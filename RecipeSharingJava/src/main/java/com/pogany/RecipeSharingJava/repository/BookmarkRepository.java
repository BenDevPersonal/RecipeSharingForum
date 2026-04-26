package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.Bookmark;
import com.pogany.RecipeSharingJava.entity.BookmarkId;
import com.pogany.RecipeSharingJava.entity.Post;
import com.pogany.RecipeSharingJava.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
    boolean existsByUserAndPost(User user, Post post);

    List<Bookmark> findByUser(User user);

    void deleteByUserAndPost(User user, Post post);

    boolean existsById(BookmarkId id);

    void deleteById(BookmarkId id);
}