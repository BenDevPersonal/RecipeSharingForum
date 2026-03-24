package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.entities.Post;
import com.pogany.RecipeSharingJava.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Post findById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void delete(Integer id) {
        postRepository.deleteById(id);
    }
}