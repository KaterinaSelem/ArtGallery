package com.example.ArtGallery.controllers;

import com.example.ArtGallery.domain.DTO.WorkDTO;
import com.example.ArtGallery.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/works")
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
        WorkDTO createdWork = workService.createWork(workDTO);
        return new ResponseEntity<>(createdWork, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkDTO> updateWork(@PathVariable Long id, @RequestBody WorkDTO workDTO) {
        WorkDTO updatedWork = workService.updateWork(id, workDTO);
        if (updatedWork != null) {
            return new ResponseEntity<>(updatedWork, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWork(@PathVariable Long id) {
        WorkDTO deletedWork = workService.deleteWork(id);
        if (deletedWork != null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}