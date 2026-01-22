package com.pogany.recipesharingforum.entities;

public class User {
    private int id;
    private String login;
    private String password;
    private String email;
    private String country;

    public User() {
    }

    public User(String username, String password, String email, String country) {
        this.login = username;
        this.password = password;
        this.email = email;
        this.country = country;
    }

    public User(int id, String email, String password, String username, String country) {
        this.id = id;
        this.login = username;
        this.password = password;
        this.email = email;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
