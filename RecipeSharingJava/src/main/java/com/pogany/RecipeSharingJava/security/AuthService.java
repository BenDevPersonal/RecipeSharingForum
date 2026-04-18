package com.pogany.RecipeSharingJava.security;

import com.pogany.RecipeSharingJava.entity.*;
import com.pogany.RecipeSharingJava.repository.*;
import com.pogany.RecipeSharingJava.security.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CountryRepository countryRepository;
    private final AllergyRepository allergyRepository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       CountryRepository countryRepository,
                       AllergyRepository allergyRepository,
                       JwtService jwtService,
                       PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.countryRepository = countryRepository;
        this.allergyRepository = allergyRepository;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    public String login(String login, String password) {

        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return jwtService.generateToken(
                user.getLogin(),
                user.getRole().getName()
        );
    }

    public String register(RegisterRequest req) {

        if (userRepository.findByLogin(req.getLogin()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists"
            );
        }

        Country country = countryRepository.findById(req.getCountryCode())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Country not found"));


        Role role = roleRepository.findByName("user")
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role missing"));

        List<Integer> ids = req.getAllergyIds()
                .stream()
                .map(Long::intValue)
                .toList();

        List<Allergy> allergies = allergyRepository.findAllById(ids);

        User user = new User();
        user.setLogin(req.getLogin());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setCountry(country);
        user.setRole(role);
        user.setAllergies(allergies);

        User saved = userRepository.save(user);

        return jwtService.generateToken(
                saved.getLogin(),
                saved.getRole().getName()
        );
    }
}