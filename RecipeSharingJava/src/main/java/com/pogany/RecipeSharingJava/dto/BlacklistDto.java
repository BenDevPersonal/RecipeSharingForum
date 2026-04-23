package com.pogany.RecipeSharingJava.dto;

public class BlacklistDto {
    private Integer id;
    private Integer blacklistedUserId;
    private String blaclistedUser;

    public BlacklistDto() {
    }

    public BlacklistDto(Integer id, Integer blacklistedUserId, String blaclistedUser) {
        this.id = id;
        this.blacklistedUserId = blacklistedUserId;
        this.blaclistedUser = blaclistedUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlacklistedUserId() {
        return blacklistedUserId;
    }

    public void setBlacklistedUserId(Integer blacklistedUserId) {
        this.blacklistedUserId = blacklistedUserId;
    }

    public String getBlaclistedUser() {
        return blaclistedUser;
    }

    public void setBlaclistedUser(String blaclistedUser) {
        this.blaclistedUser = blaclistedUser;
    }
}
