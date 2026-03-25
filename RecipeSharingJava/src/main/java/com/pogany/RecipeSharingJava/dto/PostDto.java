package com.pogany.RecipeSharingJava.dto;

import java.time.LocalDate;

public class PostDto {
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private LocalDate creationDate;
    private LocalDate updateDate;

    public PostDto() {
    }

    public PostDto(Integer id, Integer userId, String title, String content, LocalDate creationDate, LocalDate updateDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}
