package com.pogany.recipesharingforum.recipesharingjava.dao;

import com.pogany.recipesharingforum.recipesharingjava.entities.User;
import com.pogany.recipesharingforum.recipesharingjava.utilities.TransactionManager;

import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public void createUser(User user) {
        TransactionManager.executeVoid(em -> {
            em.persist(user);
            return null;
        });
    }

    @Override
    public void updateUser(User user) {
        TransactionManager.executeVoid(em -> {
            em.merge(user);
            return null;
        });
    }

    @Override
    public void removeUser(User user) {
        TransactionManager.executeVoid(em -> {
            em.remove(em.merge(user));
            return null;
        });
    }

    @Override
    public List<User> findAll() {
        return TransactionManager.execute(em ->
                em.createQuery("SELECT u FROM User u", User.class)
                        .getResultList()
        );
    }

    @Override
    public User findById(int id) {
        return TransactionManager.execute(em -> em.find(User.class, id));
    }

    @Override
    public User findByLogin(String login) {
        return TransactionManager.execute(em -> {
            List<User> list = em.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        });
    }

    @Override
    public User findByEmail(String email) {
        return TransactionManager.execute(em -> {
            List<User> list = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        });
    }

    @Override
    public User authUser(String login, String password) {
        return TransactionManager.execute(em -> {
            List<User> list = em.createQuery(
                            "SELECT u FROM User u WHERE u.login = :login AND u.password = :password",
                            User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        });
    }
}