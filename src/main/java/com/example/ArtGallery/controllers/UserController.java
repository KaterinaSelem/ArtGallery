package com.example.ArtGallery.controllers;

import com.example.ArtGallery.domain.DTO.*;
import com.example.ArtGallery.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/artists/{id}")
    public UserArtistDTO getUserArtistById(@PathVariable Long id) {
        return userService.getUserArtistById(id);
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


    @PutMapping("/updateUserImage")
    public ResponseEntity<String> updateUserAndUploadImage(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "liveCity", required = false) String liveCity,
            @RequestParam(value = "bornCity", required = false) String bornCity,
            @RequestParam(value = "name", required = false) String name) {

        // Получаем текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Email из токена

        // Ищем пользователя по email
        UserDTO currentUser = userService.getUserByEmail(currentUserEmail);
        if (currentUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Флаг, указывающий, были ли обновлены текстовые данные
        boolean isTextUpdated = false;

        // Обновляем текстовые данные пользователя, если предоставлены новые данные
        if (description != null || liveCity != null || bornCity != null || name != null) {
            if (description != null) currentUser.setDescription(description);
            if (liveCity != null) currentUser.setLiveCity(liveCity);
            if (bornCity != null) currentUser.setBornCity(bornCity);
            if (name != null) currentUser.setName(name);

            // Сохраняем обновления в базе данных
            userService.updateUser(currentUser.getId(), currentUser);
            isTextUpdated = true;
        }

        // Если есть файл изображения, загружаем его
        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = userService.uploadUserImage(currentUser.getEmail(), file);
                // Обновляем URL изображения в данных пользователя
                currentUser.setImage(imageUrl);
                userService.updateUser(currentUser.getId(), currentUser);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
            }
        }

        if (isTextUpdated || file != null) {
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No updates were provided", HttpStatus.OK);
        }
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