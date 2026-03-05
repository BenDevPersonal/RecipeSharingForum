package com.pogany.recipesharingforum.recipesharingjava.dao;

import com.pogany.recipesharingforum.recipesharingjava.entities.Role;
import com.pogany.recipesharingforum.recipesharingjava.utilities.TransactionManager;

import java.util.List;

public class RoleDaoImpl implements RoleDao {

    @Override
    public void createRole(Role role) {
        TransactionManager.executeVoid(em -> {
            em.persist(role);
            return null;
        });
    }

    @Override
    public void updateRole(Role role) {
        TransactionManager.executeVoid(em -> {
            em.merge(role);
            return null;
        });
    }

    @Override
    public void removeRole(Role role) {
        TransactionManager.executeVoid(em -> {
            em.remove(em.merge(role));
            return null;
        });
    }

    @Override
    public List<Role> findAll() {
        return TransactionManager.execute(em ->
                em.createQuery("SELECT r FROM Role r", Role.class)
                        .getResultList()
        );
    }

    @Override
    public Role findById(int id) {
        return TransactionManager.execute(em -> em.find(Role.class, id));
    }
}