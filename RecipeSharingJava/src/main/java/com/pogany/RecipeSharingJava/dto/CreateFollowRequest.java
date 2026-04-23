package com.pogany.RecipeSharingJava.dto;

public class CreateFollowRequest {
    private Integer followedUserId;

    public CreateFollowRequest() {
    }

    public Integer getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(Integer followedUserId) {
        this.followedUserId = followedUserId;
    }
}
