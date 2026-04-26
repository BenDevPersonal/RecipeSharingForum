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
    private boolean bookmarked;

    public PostDto() {
    }

    public PostDto(
            Integer id,
            Integer userId,
            String author,
            String title,
            String content,
            LocalDate creationDate,
            LocalDate updateDate,
            List<String> allergies,
            List<String> categories,
            List<FeedbackDto> feedbacks,
            boolean bookmarked
    ) {
        this.id = id;
        this.authorId = userId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.allergies = allergies;
        this.categories = categories;
        this.feedbacks = feedbacks;
        this.bookmarked = bookmarked;
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

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
}