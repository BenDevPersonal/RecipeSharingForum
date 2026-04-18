package com.pogany.RecipeSharingJava.dto;

import java.util.List;
import java.util.Set;

public class UserDto {
    private Integer id;
    private String login;
    private String email;
    private String country;
    private String role;
    private List<String> allergies;

    public UserDto() {
    }

    public UserDto(Integer id, String login, String email, String country, String roleName, List<String> allergies) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.country = country;
        this.role = roleName;
        this.allergies = allergies;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}
