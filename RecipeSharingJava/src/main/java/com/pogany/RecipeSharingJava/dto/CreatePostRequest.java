package com.pogany.RecipeSharingJava.dto;

import java.time.LocalDate;
import java.util.List;

public class CreatePostRequest {

    private Integer userId;
    private String title;
    private String content;
    private LocalDate creationDate;
    private LocalDate updateDate;
    private List<Integer> allergyIds;
    private List<Integer> categoryIds;

    public CreatePostRequest() {
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

    public List<Integer> getAllergyIds() {
        return allergyIds;
    }

    public void setAllergyIds(List<Integer> allergyIds) {
        this.allergyIds = allergyIds;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}