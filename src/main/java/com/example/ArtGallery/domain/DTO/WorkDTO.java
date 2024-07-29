package com.example.ArtGallery.domain.DTO;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkDTO {
    private Long id;
    private String title;
    private Date createdAt;
    private int categoryId;
    private int artStyle;
    private boolean comition;
    private Long userId; // Ссылка на User
    private String description;
    private String image;

}
