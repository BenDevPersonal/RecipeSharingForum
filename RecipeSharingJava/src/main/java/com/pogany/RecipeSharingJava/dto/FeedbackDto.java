package com.pogany.RecipeSharingJava.dto;

public class FeedbackDto {

    private Integer id;
    private Integer authorId;
    private String author;
    private Integer postId;
    private Integer rating;
    private String content;

    public FeedbackDto() {
    }

    public FeedbackDto(Integer id, Integer authorId, String author, Integer postId, Integer rating, String content) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
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

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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