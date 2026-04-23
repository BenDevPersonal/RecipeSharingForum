package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreateUserSettingRequest;
import com.pogany.RecipeSharingJava.dto.UserSettingDto;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.entity.UserSetting;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import com.pogany.RecipeSharingJava.repository.UserSettingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSettingService {
    private UserSettingRepository userSettingRepository;
    private UserRepository userRepository;

    public UserSettingService(UserSettingRepository userSettingRepository, UserRepository userRepository) {
        this.userSettingRepository = userSettingRepository;
        this.userRepository = userRepository;
    }

    public List<UserSettingDto> findALl() {
        return userSettingRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public UserSettingDto findById(Integer id) {
        return toDto(userSettingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Setting not found: " + id)));
    }

    public UserSettingDto findByUserId(Integer userId) {
        return toDto(userSettingRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Setting not found: " + userId)));
    }

    public UserSettingDto create(CreateUserSettingRequest request) {
        User user = userRepository.findByLogin(getCurrentUser().getLogin())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + getCurrentUser().getLogin()));

        UserSetting userSetting = new UserSetting();

        userSetting.setUser(user);
        userSetting.setShowCountryOnProfile(request.isShowCountryOnProfile());
        userSetting.setShowAllergyOnProfile(request.isShowAllergyOnProfile());
        userSetting.setAutoFilterAllergy(request.isAutoFilterAllergy());

        return toDto(userSettingRepository.save(userSetting));
    }

    public UserSettingDto update(Integer id, CreateUserSettingRequest request) {
        UserSetting userSetting = userSettingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSetting not found: " + id));

        User user = getCurrentUser();

        userSetting.setUser(user);
        userSetting.setShowCountryOnProfile(request.isShowCountryOnProfile());
        userSetting.setShowAllergyOnProfile(request.isShowAllergyOnProfile());
        userSetting.setAutoFilterAllergy(request.isAutoFilterAllergy());

        return toDto(userSettingRepository.save(userSetting));
    }

    public void delete(Integer id) {
        UserSetting userSetting = userSettingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSetting not found: " + id));

        userSettingRepository.delete(userSetting);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + login));
    }

    private UserSettingDto toDto(UserSetting userSetting) {
        return new UserSettingDto(
                userSetting.getId(),
                userSetting.getShowCountryOnProfile(),
                userSetting.getShowAllergyOnProfile(),
                userSetting.getAutoFilterAllergy()
        );
    }
}
