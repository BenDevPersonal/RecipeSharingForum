package com.pogany.recipesharingforum.recipesharingjava.utilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.function.Function;

public class TransactionManager {

    public static <R> R execute(Function<EntityManager, R> function) {
        EntityManager em = HibernateUtil.getSessionFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            R result = function.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static void executeVoid(Function<EntityManager, Void> function) {
        execute(em -> {
            function.apply(em);
            return null;
        });
    }
}