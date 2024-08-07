package com.example.ArtGallery.repositories;


import com.example.ArtGallery.domain.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    List<Work> findByCategoryId(Long categoryId);
}
