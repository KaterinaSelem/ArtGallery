package com.example.ArtGallery.domain.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "work")
@Data
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "art_style")
    private int artStyle;

    @Column(name = "comition")
    private boolean comition;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonBackReference
    private User user;
}
