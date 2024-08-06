package com.example.ArtGallery.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDeleteDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Set<RoleDTO> roles;
}
