package com.pogany.RecipeSharingJava.security;

import java.util.List;

public class RegisterRequest {

    private String login;
    private String email;
    private String password;
    private String countryCode;
    private List<Long> allergyIds;

    public String getLogin() { return login; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getCountryCode() { return countryCode; }
    public List<Long> getAllergyIds() { return allergyIds; }
}