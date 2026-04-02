package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreateUserRequest;
import com.pogany.RecipeSharingJava.dto.UserDto;
import com.pogany.RecipeSharingJava.entity.Role;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.RoleRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

    public UserDto createUser(CreateUserRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + request.getRoleId()));

        User user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCountry(request.getCountry());
        user.setRole(role);

        return toDto(userRepository.save(user));
    }

    public UserDto updateUser(Integer id, CreateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + request.getRoleId()));

        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCountry(request.getCountry());
        user.setRole(role);

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
                user.getCountry(),
                user.getRole().getName()
        );
    }
}