package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.entity.BookmarkId;
import org.springframework.stereotype.Service;
import java.util.List;
import com.pogany.RecipeSharingJava.entity.Bookmark;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.entity.Post;
import com.pogany.RecipeSharingJava.repository.BookmarkRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import com.pogany.RecipeSharingJava.repository.PostRepository;


@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository,
                           UserRepository userRepository,
                           PostRepository postRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public void toggleBookmark(Integer userId, Integer postId) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        Post post = postRepository.findById(postId)
                .orElseThrow();

        BookmarkId id = new BookmarkId(user.getId(), post.getId());

        if (bookmarkRepository.existsById(id)) {
            bookmarkRepository.deleteById(id);
        } else {
            Bookmark b = new Bookmark(user, post);
            bookmarkRepository.save(b);
        }
    }

    public List<Post> getBookmarkedPosts(User user) {
        return bookmarkRepository.findByUser(user)
                .stream()
                .map(Bookmark::getPost)
                .toList();
    }
}
