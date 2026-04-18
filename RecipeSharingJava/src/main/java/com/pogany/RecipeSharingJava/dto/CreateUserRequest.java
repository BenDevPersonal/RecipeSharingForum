package com.pogany.RecipeSharingJava.dto;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public class CreateUserRequest {
    private String login;
    private String email;
    private String password;
    private String country;
    private String role;
    private List<Integer> allergies;

    public CreateUserRequest() {}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Integer> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Integer> allergies) {
        this.allergies = allergies;
    }
}