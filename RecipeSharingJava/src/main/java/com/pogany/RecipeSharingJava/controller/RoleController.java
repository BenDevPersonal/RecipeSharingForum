package com.pogany.RecipeSharingJava.controller;

import com.pogany.RecipeSharingJava.dto.RoleDto;
import com.pogany.RecipeSharingJava.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDto> getAllRoles() { return roleService.findAll(); }

    @GetMapping("/{id}")
    public RoleDto getRole(@PathVariable Integer id) { return roleService.findById(id); }
}
