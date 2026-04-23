package com.pogany.RecipeSharingJava.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "blacklist")
public class Blacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blacklisting_user", nullable = false)
    private User blacklistingUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blacklisted_user", nullable = false)
    private User blacklistedUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getBlacklistingUser() {
        return blacklistingUser;
    }

    public void setBlacklistingUser(User blacklistingUser) {
        this.blacklistingUser = blacklistingUser;
    }

    public User getBlacklistedUser() {
        return blacklistedUser;
    }

    public void setBlacklistedUser(User blacklistedUser) {
        this.blacklistedUser = blacklistedUser;
    }
}