package com.pogany.RecipeSharingJava.dto;

import com.pogany.RecipeSharingJava.enums.NotificationType;

import java.time.LocalDate;
import java.util.Map;

public class CreateNotificationRequest {

    private NotificationType type;

    private Integer postId;

    private Map<String, Object> metadata;

    public CreateNotificationRequest() {}

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}