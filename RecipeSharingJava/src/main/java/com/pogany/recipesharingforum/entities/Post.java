package com.pogany.recipesharingforum.entities;

import java.sql.Date;

public class Post {
    private int id;
    private int userId;
    private String title;
    private String content;
    private Date creationDate;
    private Date updateDate;

    public Post() {
    }

    public Post(int userId, String title, String content, Date creationDate, Date updateDate) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public Post(int id, int userId, String title, String content, Date creationDate, Date updateDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
