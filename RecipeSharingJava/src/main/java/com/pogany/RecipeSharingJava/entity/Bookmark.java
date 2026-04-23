package com.pogany.RecipeSharingJava.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bookmark")
public class Bookmark {

    @EmbeddedId
    private BookmarkId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    public Bookmark() {}

    public Bookmark(User user, Post post) {
        this.user = user;
        this.post = post;
        this.id = new BookmarkId(user.getId(), post.getId()); // 🔥 important
    }

    public BookmarkId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    // FIXED SETTERS
    public void setUser(User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    public void setId(BookmarkId id) {
        this.id = id;
    }
}