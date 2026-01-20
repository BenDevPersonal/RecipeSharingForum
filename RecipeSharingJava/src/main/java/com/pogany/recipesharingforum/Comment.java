package com.pogany.recipesharingforum;

public class Comment {
    private int id;
    private int userId;
    private int postId;
    private int rating;
    private String content;

    public Comment() {
    }

    public Comment(int userId, int postId, int rating, String content) {
        this.userId = userId;
        this.postId = postId;
        this.rating = rating;
        this.content = content;
    }

    public Comment(int id, int userId, int postId, int rating, String content) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.rating = rating;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
