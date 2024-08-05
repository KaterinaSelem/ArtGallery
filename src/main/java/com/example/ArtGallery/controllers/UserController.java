package com.example.ArtGallery.controllers;

import com.example.ArtGallery.domain.DTO.RegisterDTO;
import com.example.ArtGallery.domain.DTO.UserDTO;
import com.example.ArtGallery.domain.DTO.UserDeleteDTO;
import com.example.ArtGallery.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;



    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public UserDeleteDTO deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }



//    @PostMapping("/register")
//    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
//        UserDTO createdUser = userService.registerUser(userDTO);
//        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//    }

//    @PostMapping("/register")
//    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterDTO registerDTO) {
//        UserDTO createdUser = userService.registerUser(registerDTO);
//        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//    }

    @PutMapping("/{id}/updateFields")
    public ResponseEntity<UserDTO> updateUserFields(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserFields(id, userDTO);
        if (updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}