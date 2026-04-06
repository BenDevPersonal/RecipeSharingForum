package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreateUserRequest;
import com.pogany.RecipeSharingJava.dto.UserDto;
import com.pogany.RecipeSharingJava.entity.Allergy;
import com.pogany.RecipeSharingJava.entity.Country;
import com.pogany.RecipeSharingJava.entity.Role;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.AllergyRepository;
import com.pogany.RecipeSharingJava.repository.CountryRepository;
import com.pogany.RecipeSharingJava.repository.RoleRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CountryRepository countryRepository;
    private AllergyRepository allergyRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, CountryRepository countryRepository, AllergyRepository allergyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.countryRepository = countryRepository;
        this.allergyRepository = allergyRepository;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public UserDto findById(Integer id) {
        return toDto(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id)));
    }

    public UserDto findByLogin(String login) {
        return toDto(userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Login: " + login)));
    }

    public UserDto createUser(CreateUserRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + request.getRoleId()));

        Country country = countryRepository.findById(request.getCountry())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with Code: " + request.getCountry()));

        User user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCountry(country);
        user.setRole(role);

        if (request.getAllergyIds() != null && !request.getAllergyIds().isEmpty()) {
            Set<Allergy> allergies = new HashSet<>(allergyRepository.findAllById(request.getAllergyIds()));
            user.setAllergies(allergies);
        } else {
            user.setAllergies(new HashSet<>());
        }

        return toDto(userRepository.save(user));
    }

    public UserDto updateUser(Integer id, CreateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + request.getRoleId()));

        Country country = countryRepository.findById(request.getCountry())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with Code: " + request.getCountry()));

        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCountry(country);
        user.setRole(role);

        if (request.getAllergyIds() != null && !request.getAllergyIds().isEmpty()) {
            Set<Allergy> allergies = new HashSet<>(allergyRepository.findAllById(request.getAllergyIds()));
            user.setAllergies(allergies);
        } else {
            user.setAllergies(new HashSet<>());
        }

        userRepository.save(user);

        return toDto(user);
    }

    public void delete(Integer id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        userRepository.deleteById(id);
    }

    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getCountry().getName(),
                user.getRole().getName(),
                user.getAllergies().stream().map(Allergy::getName).collect(Collectors.toSet())
        );
    }
}