package com.example.ArtGallery.repositories;


import com.example.ArtGallery.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    User deleteById(Long id);

}