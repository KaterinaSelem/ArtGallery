package com.example.ArtGallery.domain.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Set<RoleDTO> roles;
    private String bornCity;
    private String liveCity;
    private String description;
    private String image;
}
