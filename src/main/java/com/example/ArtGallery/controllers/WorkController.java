package com.example.ArtGallery.controllers;

import com.example.ArtGallery.domain.DTO.WorkDTO;
import com.example.ArtGallery.domain.DTO.WorkDisplayDTO;
import com.example.ArtGallery.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/works")
public class WorkController {

    @Autowired
    private WorkService workService;


    @GetMapping
    public ResponseEntity<List<WorkDTO>> getAllWorks() {
        List<WorkDTO> works = workService.getAllWorks();
        return new ResponseEntity<>(works, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkDTO> getWorkById(@PathVariable Long id) {
        WorkDTO work = workService.getWorkById(id);
        if (work != null) {
            return new ResponseEntity<>(work, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<WorkDTO> createWork(@RequestBody WorkDTO workDTO) {
        // Получаем email текущего пользователя из токена
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Передаем email текущего пользователя в сервис для получения userId
        WorkDTO createdWork = workService.createWork(workDTO, currentUserEmail);
        return new ResponseEntity<>(createdWork, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkDTO> updateWork(@PathVariable Long id, @RequestBody WorkDTO workDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        WorkDTO updatedWork = workService.updateWork(id, workDTO, currentUserEmail);
        if (updatedWork != null) {
            return new ResponseEntity<>(updatedWork, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Если пользователь не является владельцем
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWork(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        boolean isDeleted = workService.deleteWork(id, currentUserEmail);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Если пользователь не является владельцем
    }



    @GetMapping("/byCategory")
    public ResponseEntity<List<WorkDisplayDTO>> getWorksByCategoryId(@RequestParam Long categoryId) {
        List<WorkDisplayDTO> works = workService.getWorksByCategoryId(categoryId);
        return new ResponseEntity<>(works, HttpStatus.OK);
    }
}