package com.example.ArtGallery.service;

import com.example.ArtGallery.domain.DTO.RoleDTO;
import com.example.ArtGallery.domain.DTO.UserDTO;
import com.example.ArtGallery.domain.entity.Role;
import com.example.ArtGallery.domain.entity.User;
import com.example.ArtGallery.repositories.RoleRepository;
import com.example.ArtGallery.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    public Optional<User> getUserById(Long id) {
//        return userRepository.findById(id);
//    }
//
//    public User createUser(User user) {
//        return userRepository.save(user);
//    }
//
//    public User updateUser(Long id, User userDetails) {
//        User user = userRepository.findById(id).orElseThrow();
//        user.setName(userDetails.getName());
//        user.setEmail(userDetails.getEmail());
//        user.setPassword(userDetails.getPassword());
//        user.setUserRole(userDetails.getUserRole());
//        return userRepository.save(user);
//    }
//
//    @Transactional
//    public Optional<User> deleteUser(Long id) {
//        Optional<User> user = userRepository.findById(id);
//        user.ifPresent(userRepository::delete);
//        return user;
//    }

    @Autowired
    private RoleRepository roleRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userDTO.getName());
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setPassword(userDTO.getPassword());
                    Role role = roleRepository.findById(userDTO.getUserRole().getId()).orElse(null);
                    existingUser.setUserRole(role);
                    User updatedUser = userRepository.save(existingUser);
                    return convertToDTO(updatedUser);
                })
                .orElse(null);
    }

    @Transactional
    public UserDTO deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(id);
                    return new UserDTO(
                                                user.getId(),
                                                user.getName(),
                                                user.getEmail(),
                                                user.getPassword(),
                            new RoleDTO(
                                                        user.getUserRole().getId(),
                                                        user.getUserRole().getTitle()
                                                ),
                                                user.getLive_city(),
                                                user.getDescription(),
                                                user.getExhibitions(),
                                                user.getImage(),
                            user.getBorn_city()
                                        );
                })
                .orElse(null);
    }

    public UserDTO registerUser(UserDTO userDTO) {
        Role defaultRole = roleRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Role with ID 2 not found"));

        // Создаем нового пользователя и присваиваем ему роль по умолчанию
        User newUser = new User();
        newUser.setName(userDTO.getName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(userDTO.getPassword());
        newUser.setUserRole(defaultRole);

        // Сохраняем пользователя в базе данных
        User savedUser = userRepository.save(newUser);
        return convertToDTO(savedUser);
    }


    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        if (user.getUserRole() != null) {
            userDTO.setUserRole(new RoleDTO(user.getUserRole().getId(), user.getUserRole().getTitle()));
        }
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        if (userDTO.getUserRole() != null) {
            Role role = roleRepository.findById(userDTO.getUserRole().getId()).orElse(null);
            user.setUserRole(role);
        }
        return user;
    }
}