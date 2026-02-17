package com.pogany.recipesharingforum.recipesharingjava.entities;

public class User {
    private int id;
    private int roleId;
    private String login;
    private String password;
    private String email;
    private String country;

    public User() {
    }

    public User(String login, String password, String email, String country, int roleId) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.country = country;
        this.roleId = roleId;
    }

    public User(int id, String login, String password, String email, String country, int roleId) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.country = country;
        this.roleId = roleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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
