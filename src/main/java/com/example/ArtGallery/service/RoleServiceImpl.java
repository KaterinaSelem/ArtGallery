package com.example.ArtGallery.service;

import com.example.ArtGallery.domain.DTO.RoleDTO;
import com.example.ArtGallery.domain.entity.Role;
import com.example.ArtGallery.repositories.RoleRepository;
import com.example.ArtGallery.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.repository = roleRepository;
    }

    public List<RoleDTO> getAllRoles() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //    public RoleDTO getRoleById(Long id) {
//        return repository.findById(id)
//                .map(this::convertToDTO)
//                .orElse(null);
//    }
    @Override
    public RoleDTO getRoleDTOById(Long roleId) {
        Role role = getRoleById(roleId);
        return new RoleDTO(role.getId(), role.getTitle());
    }

    @Override
    public Role getRoleById(Long roleId) {
        return repository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }


    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = convertToEntity(roleDTO);
        Role savedRole = repository.save(role);
        return convertToDTO(savedRole);
    }

    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        return repository.findById(id)
                .map(existingRole -> {
                    existingRole.setTitle(roleDTO.getTitle());
                    Role updatedRole = repository.save(existingRole);
                    return convertToDTO(updatedRole);
                })
                .orElse(null);
    }

    public RoleDTO deleteRole(Long id) {
        return repository.findById(id)
                .map(role -> {
                    repository.deleteById(id);
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

    @Override
    public Role getRoleUser() {
        return repository.findByTitle("USER").orElseThrow(
                () -> new RuntimeException("Database doesn't contain ROLE_USER")
        );
    }


}