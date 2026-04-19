package com.pogany.RecipeSharingJava.dto;

public class CreateFollowRequest {
    private Integer followingUserId;
    private Integer followedUserId;

    public CreateFollowRequest() {
    }

    public Integer getFollowingUserId() {
        return followingUserId;
    }

    public void setFollowingUserId(Integer followingUserId) {
        this.followingUserId = followingUserId;
    }

    public Integer getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(Integer followedUserId) {
        this.followedUserId = followedUserId;
    }
}
