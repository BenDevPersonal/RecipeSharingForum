package com.pogany.RecipeSharingJava.dto;

import java.time.LocalDate;
import java.util.List;

public class PostDto {

    private Integer id;
    private Integer authorId;
    private String author;
    private String title;
    private String content;
    private LocalDate creationDate;
    private LocalDate updateDate;
    private List<String> allergies;
    private List<String> categories;
    private List<FeedbackDto> feedbacks;

    public PostDto() {
    }

    public PostDto(
            Integer id,
            Integer authorId,
            String author,
            String title,
            String content,
            LocalDate creationDate,
            LocalDate updateDate,
            List<String> allergies,
            List<String> categories,
            List<FeedbackDto> feedbacks
    ) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.allergies = allergies;
        this.categories = categories;
        this.feedbacks = feedbacks;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<FeedbackDto> getFeedbacks() {
        return feedbacks;
    }
}