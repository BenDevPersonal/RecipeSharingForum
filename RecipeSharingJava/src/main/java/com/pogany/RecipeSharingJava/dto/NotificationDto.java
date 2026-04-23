package com.pogany.RecipeSharingJava.dto;

import com.pogany.RecipeSharingJava.enums.NotificationType;

import java.time.LocalDate;
import java.util.Map;

public class NotificationDto {
    private Integer id;
    private NotificationType type;
    private String message;
    private boolean isRead;
    private LocalDate createdAt;
    private Integer postId;
    private String postTitle;
    private Integer actorId;
    private String actorName;
    private Map<String, Object> metadata;

    public NotificationDto() {
    }

    public NotificationDto(Integer id, NotificationType type, String message, boolean isRead, LocalDate createdAt, Integer postId, String postTitle, Integer actorId, String actorName, Map<String, Object> metadata) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.postId = postId;
        this.postTitle = postTitle;
        this.actorId = actorId;
        this.actorName = actorName;
        this.metadata = metadata;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
