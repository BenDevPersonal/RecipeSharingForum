package com.pogany.RecipeSharingJava.dto;

public class CreateBlacklistRequest {
    private Integer blacklistedUserId;

    public CreateBlacklistRequest() {
    }

    public Integer getBlacklistedUserId() {
        return blacklistedUserId;
    }

    public void setBlacklistedUserId(Integer blacklistedUserId) {
        this.blacklistedUserId = blacklistedUserId;
    }
}
