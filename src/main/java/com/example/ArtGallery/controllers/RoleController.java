package com.example.ArtGallery.controllers;

import com.example.ArtGallery.domain.DTO.RoleDTO;
import com.example.ArtGallery.service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

//    @GetMapping
//    public List<Role> getAllRoles() {
//        return roleService.getAllRoles();
//    }
//
//    @GetMapping("/{id}")
//    public Optional<Role> getRoleById(@PathVariable Long id) {
//        return roleService.getRoleById(id);
//    }
//
//    @PostMapping
//    public Role createRole(@RequestBody Role role) {
//        return roleService.createRole(role);
//    }
//
//    @PutMapping("/{id}")
//    public Role updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
//        return roleService.updateRole(id, roleDetails);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteRole(@PathVariable Long id) {
//        roleService.deleteRole(id);
//    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        RoleDTO role = roleService.getRoleById(id);
        if (role != null) {
            return new ResponseEntity<>(role, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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