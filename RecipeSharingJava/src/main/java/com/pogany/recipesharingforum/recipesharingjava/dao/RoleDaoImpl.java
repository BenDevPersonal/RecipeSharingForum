package com.pogany.recipesharingforum.recipesharingjava.dao;

import com.pogany.recipesharingforum.recipesharingjava.entities.Role;
import com.pogany.recipesharingforum.recipesharingjava.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    private Connection conn;
    private PreparedStatement getAllRolesPstmt;
    private PreparedStatement getRoleByIdPstmt;

    public RoleDaoImpl(Connection conn) throws SQLException {
        this.conn = conn;
        getAllRolesPstmt = conn.prepareStatement("SELECT * FROM role");
        getRoleByIdPstmt = conn.prepareStatement("SELECT * FROM role WHERE id=?");
    }

    @Override
    public void createRole(Role role) throws SQLException {
        PreparedStatement createRolePstmt = conn.prepareStatement("INSERT INTO role " +
                "(name) VALUES (?)");
        createRolePstmt.setString(1, role.getName());

        createRolePstmt.executeUpdate();

    }

    @Override
    public void updateRole(Role role) throws SQLException {
        PreparedStatement updateRolePstmt = conn.prepareStatement("UPDATE role " +
                "SET name = ? WHERE id = ?");

        updateRolePstmt.setString(1, role.getName());

        updateRolePstmt.setLong(2, role.getId());

        updateRolePstmt.executeUpdate();
    }

    @Override
    public void removeRole(Role role) throws SQLException {
        PreparedStatement removeRolePstmt = conn.prepareStatement("DELETE FROM role " +
                "WHERE id = ?");

        removeRolePstmt.setLong(1, role.getId());

        removeRolePstmt.executeUpdate();
    }

    @Override
    public List<Role> findAll() throws SQLException {
        ResultSet rs = getAllRolesPstmt.executeQuery();
        List<Role> roles = new ArrayList<>();

        while (rs.next()) {
            roles.add(new Role(
                    rs.getInt("id"),
                    rs.getString("name")
            ));
        }

        return roles;
    }

    @Override
    public Role findById(int id) throws SQLException {
        getRoleByIdPstmt.setInt(1, id);
        ResultSet rs = getRoleByIdPstmt.executeQuery();

        if (rs.next()) {
            return new Role(
                    rs.getInt("id"),
                    rs.getString("name")
            );
        }

        return null;
    }
}
