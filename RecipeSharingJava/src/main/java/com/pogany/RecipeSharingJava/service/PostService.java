package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.CreatePostRequest;
import com.pogany.RecipeSharingJava.dto.PostDto;
import com.pogany.RecipeSharingJava.entity.Allergy;
import com.pogany.RecipeSharingJava.entity.Category;
import com.pogany.RecipeSharingJava.entity.Post;
import com.pogany.RecipeSharingJava.entity.User;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.AllergyRepository;
import com.pogany.RecipeSharingJava.repository.CategoryRepository;
import com.pogany.RecipeSharingJava.repository.PostRepository;
import com.pogany.RecipeSharingJava.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private AllergyRepository allergyRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository, CategoryRepository categoryRepository, AllergyRepository allergyRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.allergyRepository = allergyRepository;
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

        if (request.getAllergyIds() != null && !request.getAllergyIds().isEmpty()) {
            Set<Allergy> allergies = new HashSet<>(allergyRepository.findAllById(request.getAllergyIds()));
            post.setAllergies(allergies);
        } else {
            post.setAllergies(new HashSet<>());
        }

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));
            post.setCategories(categories);
        } else {
            post.setCategories(new HashSet<>());
        }

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

        if (request.getAllergyIds() != null && !request.getAllergyIds().isEmpty()) {
            Set<Allergy> allergies = new HashSet<>(allergyRepository.findAllById(request.getAllergyIds()));
            post.setAllergies(allergies);
        } else {
            post.setAllergies(new HashSet<>());
        }

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));
            post.setCategories(categories);
        } else {
            post.setCategories(new HashSet<>());
        }

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
                post.getUpdateDate(),
                post.getAllergies().stream().map(Allergy::getName).collect(Collectors.toSet()),
                post.getCategories().stream().map(Category::getName).collect(Collectors.toSet())
        );
    }
}