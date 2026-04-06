package com.pogany.RecipeSharingJava.dto;

import java.time.LocalDate;
import java.util.Set;

public class CreatePostRequest {
    private Integer userId;
    private String title;
    private String content;
    private LocalDate creationDate;
    private LocalDate updateDate;
    private Set<Integer> allergyIds;
    private Set<Integer> categoryIds;

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

    public Set<Integer> getAllergyIds() {
        return allergyIds;
    }

    public void setAllergyIds(Set<Integer> allergyIds) {
        this.allergyIds = allergyIds;
    }

    public Set<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
