package com.example.ArtGallery.service;

import com.example.ArtGallery.domain.DTO.RoleDTO;
import com.example.ArtGallery.domain.entity.Role;
import com.example.ArtGallery.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

//    public List<Role> getAllRoles() {
//        return roleRepository.findAll();
//    }
//
//    public Optional<Role> getRoleById(Long id) {
//        return roleRepository.findById(id);
//    }
//
//    public Role createRole(Role role) {
//        return roleRepository.save(role);
//    }
//
//    public Role updateRole(Long id, Role roleDetails) {
//        Role role = roleRepository.findById(id).orElseThrow();
//        role.setTitle(roleDetails.getTitle());
//        return roleRepository.save(role);
//    }
//
//    public void deleteRole(Long id) {
//        roleRepository.deleteById(id);
//    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = convertToEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }

    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        return roleRepository.findById(id)
                .map(existingRole -> {
                    existingRole.setTitle(roleDTO.getTitle());
                    Role updatedRole = roleRepository.save(existingRole);
                    return convertToDTO(updatedRole);
                })
                .orElse(null);
    }

    public RoleDTO deleteRole(Long id) {
        return roleRepository.findById(id)
                .map(role -> {
                    roleRepository.deleteById(id);
                    return convertToDTO(role);
                })
                .orElse(null);
    }

    private RoleDTO convertToDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setTitle(role.getTitle());
        return roleDTO;
    }

    private Role convertToEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setTitle(roleDTO.getTitle());
        return role;
    }
}