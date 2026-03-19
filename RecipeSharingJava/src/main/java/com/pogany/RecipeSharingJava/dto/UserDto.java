package com.pogany.RecipeSharingJava.dto;

import com.pogany.RecipeSharingJava.entity.Role;

public class UserDto {
    private Integer id;
    private String login;
    private String email;
    private String password;
    private String country;
    private Role role;

    public UserDto() {
    }

    public UserDto(Integer id, String login, String email, String password, String country, Role role) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.country = country;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
