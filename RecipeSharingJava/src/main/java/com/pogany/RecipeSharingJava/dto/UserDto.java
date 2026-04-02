package com.pogany.RecipeSharingJava.dto;

public class UserDto {
    private Integer id;
    private String login;
    private String email;
    private String country;
    private String roleName;

    public UserDto() {
    }

    public UserDto(Integer id, String login, String email, String country, String roleName) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.country = country;
        this.roleName = roleName;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
