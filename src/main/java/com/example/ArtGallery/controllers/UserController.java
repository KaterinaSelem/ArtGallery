package com.example.ArtGallery.controllers;

import com.example.ArtGallery.domain.DTO.*;
import com.example.ArtGallery.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
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

    @GetMapping ("/artists")
    public List<UserArtistDTO> getAllUserArtists() {
        return userService.getAllUserArtists();
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

    @PutMapping("/{id}/updateUserAdmin")
    public ResponseEntity<String> updateUserSelf(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {
        boolean isUpdated = userService.updateUserSelf(id, userUpdateDTO);
        if (isUpdated) {
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }


    @PutMapping("/updateUser")
    public ResponseEntity<String> updateUserSelf(@RequestBody UserUpdateDTO userUpdateDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Email из токена

        UserDTO currentUser = userService.getUserByEmail(currentUserEmail);

        if (currentUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        boolean isUpdated = userService.updateUserSelf(currentUser.getId(), userUpdateDTO);
        if (isUpdated) {
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed to update user", HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/{id}")
    public UserDeleteDTO deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}/updateFields")
    public ResponseEntity<UserDTO> updateUserFields(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserFields(id, userDTO);
        if (updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {

        // Получаем email текущего пользователя из токена
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Ищем пользователя по email
        UserDTO currentUser = userService.getUserByEmail(currentUserEmail);

        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Преобразуем UserDTO в UserProfileDTO
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setName(currentUser.getName());
        userProfileDTO.setBornCity(currentUser.getBornCity());
        userProfileDTO.setLiveCity(currentUser.getLiveCity());
        userProfileDTO.setDescription(currentUser.getDescription());
        userProfileDTO.setImage(currentUser.getImage());

        // Возвращаем профиль пользователя
        return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
    }

}