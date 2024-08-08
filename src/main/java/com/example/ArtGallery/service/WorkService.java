package com.example.ArtGallery.service;


import com.example.ArtGallery.domain.DTO.WorkDTO;
import com.example.ArtGallery.domain.DTO.WorkDisplayDTO;
import com.example.ArtGallery.domain.entity.Category;
import com.example.ArtGallery.domain.entity.User;
import com.example.ArtGallery.domain.entity.Work;
import com.example.ArtGallery.repositories.UserRepository;
import com.example.ArtGallery.repositories.WorkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkService {

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private UserRepository userRepository;

    public List<WorkDTO> getAllWorks() {
        return workRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public WorkDTO getWorkById(Long id) {
        return workRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public WorkDTO createWork(WorkDTO workDTO, String currentUserEmail) {
        // Получаем userId текущего пользователя
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Устанавливаем userId в WorkDTO
        workDTO.setUserId(user.getId());

        // Сохраняем работу
        Work work = convertToEntity(workDTO);
        Work savedWork = workRepository.save(work);

        return convertToDTO(savedWork);
    }

    public WorkDTO updateWork(Long id, WorkDTO workDTO, String currentUserEmail) {
        Optional<Work> workOpt = workRepository.findById(id);
        if (workOpt.isPresent()) {
            Work existingWork = workOpt.get();
            if (existingWork.getUser().getEmail().equals(currentUserEmail)) {
                existingWork.setTitle(workDTO.getTitle());
                existingWork.setCreatedAt(workDTO.getCreatedAt());

                Category category = new Category();
                category.setId(workDTO.getCategoryId());
                existingWork.setCategory(category);

                existingWork.setArtStyle(workDTO.getArtStyle());
                existingWork.setComition(workDTO.isComition());
                existingWork.setDescription(workDTO.getDescription());
                existingWork.setImage(workDTO.getImage());

                Work updatedWork = workRepository.save(existingWork);
                return convertToDTO(updatedWork);
            }
        }
        return null; // Если работа не найдена или пользователь не является владельцем
    }

    @Transactional
    public boolean deleteWork(Long id, String currentUserEmail) {
        Optional<Work> workOpt = workRepository.findById(id);
        if (workOpt.isPresent()) {
            Work work = workOpt.get();
            if (work.getUser().getEmail().equals(currentUserEmail)) {
                workRepository.deleteById(id);
                return true;
            }
        }
        return false; // Если работа не найдена или пользователь не является владельцем
    }

    public List<WorkDisplayDTO> getWorksByCategoryId(Long categoryId) {
        return workRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDisplayDTO)
                .collect(Collectors.toList());
    }

    private WorkDisplayDTO convertToDisplayDTO(Work work) {
        WorkDisplayDTO workDisplayDTO = new WorkDisplayDTO();
        workDisplayDTO.setTitle(work.getTitle());
        workDisplayDTO.setCreatedAt(work.getCreatedAt());
        workDisplayDTO.setDescription(work.getDescription());
        workDisplayDTO.setImage(work.getImage());

        if (work.getUser() != null) {
            workDisplayDTO.setUserName(work.getUser().getName());
        }
        return workDisplayDTO;
    }

    private WorkDTO convertToDTO(Work work) {
        WorkDTO workDTO = new WorkDTO();
        workDTO.setId(work.getId());
        workDTO.setTitle(work.getTitle());
        workDTO.setCreatedAt(work.getCreatedAt());
        workDTO.setCategoryId(work.getCategory() != null ? work.getCategory().getId() : null);
        workDTO.setArtStyle(work.getArtStyle());
        workDTO.setComition(work.isComition());
        workDTO.setDescription(work.getDescription());
        workDTO.setImage(work.getImage());

        if (work.getUser() != null) {
            workDTO.setUserId(work.getUser().getId());
        }
        return workDTO;
    }

    private Work convertToEntity(WorkDTO workDTO) {
        Work work = new Work();
        work.setId(workDTO.getId());
        work.setTitle(workDTO.getTitle());
        work.setCreatedAt(workDTO.getCreatedAt());

        Category category = new Category();
        category.setId(workDTO.getCategoryId());
        work.setCategory(category);

        work.setArtStyle(workDTO.getArtStyle());
        work.setComition(workDTO.isComition());
        work.setDescription(workDTO.getDescription());
        work.setImage(workDTO.getImage());

        // Не устанавливаем пользователя при создании работы, если он не указан
        if (workDTO.getUserId() != null) {
            User user = userRepository.findById(workDTO.getUserId()).orElse(null);
            work.setUser(user);
        }
        return work;
    }
}