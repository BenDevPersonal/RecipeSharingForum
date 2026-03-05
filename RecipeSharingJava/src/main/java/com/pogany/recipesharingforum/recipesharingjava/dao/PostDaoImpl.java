package com.pogany.recipesharingforum.recipesharingjava.dao;

import com.pogany.recipesharingforum.recipesharingjava.entities.Post;
import com.pogany.recipesharingforum.recipesharingjava.utilities.TransactionManager;

import java.time.LocalDate;
import java.util.List;

public class PostDaoImpl implements PostDao {

    @Override
    public void createPost(Post post) {
        TransactionManager.executeVoid(em -> {
            post.setCreationDate(LocalDate.now());
            post.setUpdateDate(LocalDate.now());
            em.persist(post);
            return null;
        });
    }

    @Override
    public void updatePost(Post post) {
        TransactionManager.executeVoid(em -> {
            post.setUpdateDate(LocalDate.now());
            em.merge(post);
            return null;
        });
    }

    @Override
    public void removePost(Post post) {
        TransactionManager.executeVoid(em -> {
            em.remove(em.merge(post));
            return null;
        });
    }

    @Override
    public List<Post> findAll() {
        return TransactionManager.execute(em ->
                em.createQuery("SELECT p FROM Post p", Post.class)
                        .getResultList()
        );
    }

    @Override
    public List<Post> findByUserId(int userId) {
        return TransactionManager.execute(em ->
                em.createQuery("SELECT p FROM Post p WHERE p.user.id = :userId", Post.class)
                        .setParameter("userId", userId)
                        .getResultList()
        );
    }

    @Override
    public Post findById(int id) {
        return TransactionManager.execute(em -> em.find(Post.class, id));
    }
}