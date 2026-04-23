package com.pogany.RecipeSharingJava.dto;

public class FollowDto {
    private Integer id;
    private Integer followedUserId;
    private String followedUser;

    public FollowDto() {
    }

    public FollowDto(Integer id, Integer followedUserId, String followedUser) {
        this.id = id;
        this.followedUserId = followedUserId;
        this.followedUser = followedUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(Integer followedUserId) {
        this.followedUserId = followedUserId;
    }

    public String getFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(String followedUser) {
        this.followedUser = followedUser;
    }
}
