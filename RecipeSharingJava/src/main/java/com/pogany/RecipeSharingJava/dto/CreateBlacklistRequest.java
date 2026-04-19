package com.pogany.RecipeSharingJava.dto;

public class CreateBlacklistRequest {
    private Integer blacklistingUserId;
    private Integer blacklistedUserId;

    public CreateBlacklistRequest() {
    }

    public Integer getBlacklistingUserId() {
        return blacklistingUserId;
    }

    public void setBlacklistingUserId(Integer blacklistingUserId) {
        this.blacklistingUserId = blacklistingUserId;
    }

    public Integer getBlacklistedUserId() {
        return blacklistedUserId;
    }

    public void setBlacklistedUserId(Integer blacklistedUserId) {
        this.blacklistedUserId = blacklistedUserId;
    }
}
