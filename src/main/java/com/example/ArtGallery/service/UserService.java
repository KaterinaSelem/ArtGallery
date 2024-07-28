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
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
                    existingUser.setBornCity(userDTO.getBornCity());
                    existingUser.setLiveCity(userDTO.getLiveCity());
                    existingUser.setExhibitions(userDTO.getExhibitions());
                    existingUser.setDescription(userDTO.getDescription());
                    existingUser.setImage(userDTO.getImage());
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
                    return convertToDTO(user);
                })
                .orElse(null);
    }

    public UserDTO registerUser(UserDTO userDTO) {
        Role defaultRole = roleRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Role with ID 2 not found"));

        User newUser = new User();
        newUser.setName(userDTO.getName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(userDTO.getPassword());
        newUser.setUserRole(defaultRole);
        newUser.setBornCity(userDTO.getBornCity());
        newUser.setLiveCity(userDTO.getLiveCity());
        newUser.setExhibitions(userDTO.getExhibitions());
        newUser.setDescription(userDTO.getDescription());
        newUser.setImage(userDTO.getImage());

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
        userDTO.setBornCity(user.getBornCity());
        userDTO.setLiveCity(user.getLiveCity());
        userDTO.setExhibitions(user.getExhibitions());
        userDTO.setDescription(user.getDescription());
        userDTO.setImage(user.getImage());
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
        user.setBornCity(userDTO.getBornCity());
        user.setLiveCity(userDTO.getLiveCity());
        user.setExhibitions(userDTO.getExhibitions());
        user.setDescription(userDTO.getDescription());
        user.setImage(userDTO.getImage());
        return user;
    }
}