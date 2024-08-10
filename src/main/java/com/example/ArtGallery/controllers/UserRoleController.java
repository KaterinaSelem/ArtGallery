package com.example.ArtGallery.controllers;


import com.example.ArtGallery.domain.DTO.UserRoleDTO;
import com.example.ArtGallery.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    // Получение всех записей UserRole
    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> getAllUserRoles() {
        return ResponseEntity.ok(userRoleService.getAllUserRoles());
    }

    // Получение конкретной записи UserRole по id
    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDTO> getUserRoleById(@PathVariable Long id) {
        UserRoleDTO userRoleDTO = userRoleService.getUserRoleById(id);
        if (userRoleDTO != null) {
            return ResponseEntity.ok(userRoleDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    // Удаление записи UserRole
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        userRoleService.deleteUserRole(id);
        return ResponseEntity.noContent().build();
    }
}