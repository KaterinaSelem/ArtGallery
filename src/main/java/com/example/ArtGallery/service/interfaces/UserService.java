package com.example.ArtGallery.service.interfaces;

import com.example.ArtGallery.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void register (User user);
}
