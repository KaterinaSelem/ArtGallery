package com.example.ArtGallery.service.interfaces;


import com.example.ArtGallery.domain.DTO.RoleDTO;
import com.example.ArtGallery.domain.entity.Role;

public interface RoleService {

    Role getRoleUser();

    Role getRoleById(Long roleId);

    RoleDTO getRoleDTOById(Long roleId);
}
