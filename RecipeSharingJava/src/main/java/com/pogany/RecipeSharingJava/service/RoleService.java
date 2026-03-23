package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.entities.Role;
import com.pogany.RecipeSharingJava.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public void delete(Integer id) {
        roleRepository.deleteById(id);
    }
}