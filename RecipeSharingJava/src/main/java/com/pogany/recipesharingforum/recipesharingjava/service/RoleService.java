package com.pogany.recipesharingforum.recipesharingjava.service;

import com.pogany.recipesharingforum.recipesharingjava.dao.RoleDao;
import com.pogany.recipesharingforum.recipesharingjava.entities.Role;

import java.sql.SQLException;

public class RoleService {
    private RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Role getRole(int roleId) throws SQLException {
        return roleDao.findById(roleId);
    }
}
