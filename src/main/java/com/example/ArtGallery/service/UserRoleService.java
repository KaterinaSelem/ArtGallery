package com.example.ArtGallery.service;

import com.example.ArtGallery.domain.DTO.UserRoleDTO;
import com.example.ArtGallery.domain.entity.Role;
import com.example.ArtGallery.domain.entity.User;
import com.example.ArtGallery.domain.entity.UserRole;

import com.example.ArtGallery.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    // Преобразование UserRole в UserRoleDTO
    private UserRoleDTO convertToDTO(UserRole userRole) {
        return new UserRoleDTO(
                userRole.getId(),
                userRole.getUser().getId(),
                userRole.getRole().getId()
        );
    }

    // Получение всех записей UserRole
    public List<UserRoleDTO> getAllUserRoles() {
        return userRoleRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Получение конкретного UserRole по id
    public UserRoleDTO getUserRoleById(Long id) {
        Optional<UserRole> userRole = userRoleRepository.findById(id);
        return userRole.map(this::convertToDTO).orElse(null);
    }


    // Удаление записи UserRole
    public void deleteUserRole(Long id) {
        userRoleRepository.deleteById(id);
    }
}
