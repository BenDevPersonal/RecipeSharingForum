package com.pogany.RecipeSharingJava.dto;

import com.pogany.RecipeSharingJava.entity.Allergy;

import java.util.Set;

public class CreateUserRequest {
    private String login;
    private String email;
    private String password;
    private String country;
    private Integer roleId;
    private Set<Integer> allergyIds;

    public CreateUserRequest() {
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Set<Integer> getAllergyIds() {
        return allergyIds;
    }

    public void setAllergyIds(Set<Integer> allergyIds) {
        this.allergyIds = allergyIds;
    }
}
