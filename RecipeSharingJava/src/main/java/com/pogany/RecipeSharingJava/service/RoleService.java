package com.pogany.RecipeSharingJava.service;

import com.pogany.RecipeSharingJava.dto.AllergyDto;
import com.pogany.RecipeSharingJava.dto.RoleDto;
import com.pogany.RecipeSharingJava.entity.Allergy;
import com.pogany.RecipeSharingJava.entity.Role;
import com.pogany.RecipeSharingJava.exception.ResourceNotFoundException;
import com.pogany.RecipeSharingJava.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDto> findAll() {
        return roleRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public RoleDto findById(Integer id) {
        return toDto(roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id)));
    }

    private RoleDto toDto(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName()
        );
    }
}