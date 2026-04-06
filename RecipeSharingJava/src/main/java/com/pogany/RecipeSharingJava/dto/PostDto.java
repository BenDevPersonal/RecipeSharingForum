package com.pogany.RecipeSharingJava.dto;

import java.time.LocalDate;
import java.util.Set;

public class PostDto {
    private Integer id;
    private String author;
    private String title;
    private String content;
    private LocalDate creationDate;
    private LocalDate updateDate;
    private Set<String> allergies;
    private Set<String> categories;

    public PostDto() {
    }

    public PostDto(Integer id, String author, String title, String content, LocalDate creationDate, LocalDate updateDate, Set<String> allergies, Set<String> categories) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.allergies = allergies;
        this.categories = categories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setUserId(String author) {
        this.author = author;
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

    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(Set<String> allergies) {
        this.allergies = allergies;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }
}
