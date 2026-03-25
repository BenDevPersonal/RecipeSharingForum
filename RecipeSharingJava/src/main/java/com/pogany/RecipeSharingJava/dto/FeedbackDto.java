package com.pogany.RecipeSharingJava.dto;

public class FeedbackDto {
    private Integer id;
    private Integer userId;
    private Integer postId;
    private Integer rating;
    private String content;

    public FeedbackDto() {
    }

    public FeedbackDto(Integer id, Integer userId, Integer postId, Integer rating, String content) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.rating = rating;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
