package com.example.ArtGallery.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserArtistDTO {
    private String name;
    private String bornCity;
    private String liveCity;
    private String description;
    private String image;
    private String email;
}
