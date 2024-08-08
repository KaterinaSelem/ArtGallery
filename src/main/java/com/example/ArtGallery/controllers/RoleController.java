package com.example.ArtGallery.controllers;

import com.example.ArtGallery.domain.DTO.RoleDTO;
import com.example.ArtGallery.service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        RoleDTO updatedRole = roleService.updateRole(id, roleDTO);
        if (updatedRole != null) {
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoleDTO> deleteRole(@PathVariable Long id) {
        RoleDTO deletedRole = roleService.deleteRole(id);
        if (deletedRole != null) {
            return new ResponseEntity<>(deletedRole, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}