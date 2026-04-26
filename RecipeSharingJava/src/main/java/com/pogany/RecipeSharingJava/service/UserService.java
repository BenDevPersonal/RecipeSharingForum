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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CountryRepository countryRepository;
    private final AllergyRepository allergyRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       CountryRepository countryRepository,
                       AllergyRepository allergyRepository) {
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

    public List<UserDto> search(String q) {
        return userRepository.findAll().stream()
                .filter(user -> user.getLogin().toLowerCase().contains(q.toLowerCase()))
                .map(this::toDto)
                .toList();
    }

    public UserDto updateUser(Integer id, CreateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRole()));

        Country country = countryRepository.findByName(request.getCountry())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found: " + request.getCountry()));

        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }

        user.setCountry(country);
        user.setRole(role);

        user.setAllergies(new ArrayList<>());

        if (request.getAllergies() != null) {
            user.getAllergies().addAll(
                    allergyRepository.findAllById(request.getAllergies())
            );
        }


        return toDto(userRepository.save(user));
    }

    public UserDto createUser(CreateUserRequest request) {

        Country country = countryRepository.findByName(request.getCountry())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found: " + request.getCountry()));

        User user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCountry(country);

        if (request.getAllergies() != null && !request.getAllergies().isEmpty()) {
            List<Allergy> allergies = allergyRepository.findAll()
                    .stream()
                    .filter(a -> request.getAllergies().contains(a.getName()))
                    .toList();

            user.setAllergies(new ArrayList<>(allergies));
        } else {
            user.setAllergies(new ArrayList<>());
        }

        return toDto(userRepository.save(user));
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
                user.getAllergies().stream().map(Allergy::getName).collect(Collectors.toList())
        );
    }
}