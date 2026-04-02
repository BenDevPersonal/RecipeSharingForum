package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreatePostRequest;
import com.pogany.RecipeSharingJava.dto.PostDto;
import com.pogany.RecipeSharingJava.dto.UserDto;
import com.pogany.RecipeSharingJava.entity.Post;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.PostRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PostService {

    private final UserRepository userRepository;
    private PostRepository postRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<PostDto> findAll() {
        return postRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public PostDto findById(Integer id) {
        return toDto(postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + id)));
    }

    public PostDto createPost(CreatePostRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        Post post = new Post();
        post.setUser(user);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCreationDate(request.getCreationDate());
        post.setUpdateDate(request.getUpdateDate());

        return toDto(postRepository.save(post));
    }

    public PostDto updatePost(Integer id, CreatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + id));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        post.setUser(user);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCreationDate(request.getCreationDate());
        post.setUpdateDate(LocalDate.now());

        postRepository.save(post);

        return toDto(post);
    }

    public void delete(Integer id) {
        postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + id));

        postRepository.deleteById(id);
    }

    private PostDto toDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getUser().getLogin(),
                post.getTitle(),
                post.getContent(),
                post.getCreationDate(),
                post.getUpdateDate()

        );
    }
}