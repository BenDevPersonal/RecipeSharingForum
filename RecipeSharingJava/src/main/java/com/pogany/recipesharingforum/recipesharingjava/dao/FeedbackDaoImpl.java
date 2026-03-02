package com.pogany.recipesharingforum.recipesharingjava.dao;

import com.pogany.recipesharingforum.recipesharingjava.entities.Feedback;
import com.pogany.recipesharingforum.recipesharingjava.utilities.TransactionManager;

import java.util.List;

public class FeedbackDaoImpl implements FeedbackDao {

    @Override
    public void createComment(Feedback feedback) {
        TransactionManager.executeVoid(em -> {
            em.persist(feedback);
            return null;
        });
    }

    @Override
    public void updateComment(Feedback feedback) {
        TransactionManager.executeVoid(em -> {
            em.merge(feedback);
            return null;
        });
    }

    @Override
    public void removeComment(Feedback feedback) {
        TransactionManager.executeVoid(em -> {
            em.remove(em.merge(feedback));
            return null;
        });
    }

    @Override
    public Feedback findById(int id) {
        return TransactionManager.execute(em -> em.find(Feedback.class, id));
    }

    @Override
    public List<Feedback> findByPostId(int postId) {
        return TransactionManager.execute(em ->
                em.createQuery("SELECT f FROM Feedback f WHERE f.post.id = :postId", Feedback.class)
                        .setParameter("postId", postId)
                        .getResultList()
        );
    }

    @Override
    public List<Feedback> findByUserId(int userId) {
        return TransactionManager.execute(em ->
                em.createQuery("SELECT f FROM Feedback f WHERE f.user.id = :userId", Feedback.class)
                        .setParameter("userId", userId)
                        .getResultList()
        );
    }
}