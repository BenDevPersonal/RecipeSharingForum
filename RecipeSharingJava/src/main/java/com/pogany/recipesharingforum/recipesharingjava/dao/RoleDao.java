package com.pogany.recipesharingforum.recipesharingjava.dao;

import com.pogany.recipesharingforum.recipesharingjava.entities.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleDao {
    void createRole(Role role) throws SQLException;
    void updateRole(Role role) throws SQLException;
    void removeRole(Role role) throws SQLException;

    List<Role> findAll() throws SQLException;

    Role findById(int id) throws SQLException;
}
