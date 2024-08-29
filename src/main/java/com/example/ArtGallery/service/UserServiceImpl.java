package com.example.ArtGallery.service;

import com.example.ArtGallery.domain.DTO.*;
import com.example.ArtGallery.domain.entity.ConfirmationCode;
import com.example.ArtGallery.domain.entity.Role;
import com.example.ArtGallery.domain.entity.User;
import com.example.ArtGallery.repositories.RoleRepository;
import com.example.ArtGallery.repositories.UserRepository;
import com.example.ArtGallery.service.interfaces.EmailService;
import com.example.ArtGallery.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final RoleServiceImpl roleService;
    private final EmailService emailService;
    private final ConfirmationServiceImpl confirmationService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, RoleServiceImpl roleService, EmailService emailService, ConfirmationServiceImpl confirmationService) {
        this.repository = userRepository;
        this.encoder = encoder;
        this.roleService = roleService;
        this.emailService = emailService;
        this.confirmationService = confirmationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User %s not found", username))
        );
        if (!user.getActive()) {
            throw new UsernameNotFoundException("User is not active");
        }
        return user;
    }

    @Autowired
    private RoleRepository roleRepository;

    public List<UserDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // метод для вывода всех UserArtist и так же преобазование  convertToUserArtistDTO
    public List<UserArtistDTO> getAllUserArtists() {
        return repository.findAll().stream()
                .filter(this::hasArtistRole) // Фильтрация по наличию роли ARTIST
                .map(this::convertToUserArtistDTO)
                .collect(Collectors.toList());
    }

    private boolean hasArtistRole(User user) {
        // Проверка наличия роли ARTIST среди ролей пользователя
        return user.getRoles().stream()
                .anyMatch(role -> role.getTitle().equalsIgnoreCase("ARTIST"));
    }

    private UserArtistDTO convertToUserArtistDTO(User user) {
        return new UserArtistDTO(
                user.getName(),
                user.getBornCity(),
                user.getLiveCity(),
                user.getDescription(),
                user.getImage(),
                user.getEmail()
        );
    }


    public UserDTO getUserById(Long id) {
        return repository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = repository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return repository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userDTO.getName());
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setPassword(userDTO.getPassword());

                    // Обновляем роли
                    Set<Role> roles = userDTO.getRoles().stream()
                            .map(roleDTO -> roleRepository.findById(roleDTO.getId()).orElse(null))
                            .collect(Collectors.toSet());
                    existingUser.setRoles(roles);

                    existingUser.setBornCity(userDTO.getBornCity());
                    existingUser.setLiveCity(userDTO.getLiveCity());
                    existingUser.setDescription(userDTO.getDescription());
                    existingUser.setImage(userDTO.getImage());
                    User updatedUser = repository.save(existingUser);
                    return convertToDTO(updatedUser);
                })
                .orElse(null);
    }

    public boolean updateUserSelf(Long id, UserUpdateDTO userUpdateDTO) {
        return repository.findById(id)
                .map(existingUser -> {
                    if (userUpdateDTO.getName() != null) {
                        existingUser.setName(userUpdateDTO.getName());
                    }
                    if (userUpdateDTO.getPassword() != null) {
                        existingUser.setPassword(encoder.encode(userUpdateDTO.getPassword()));
                    }
                    if (userUpdateDTO.getBornCity() != null) {
                        existingUser.setBornCity(userUpdateDTO.getBornCity());
                    }
                    if (userUpdateDTO.getLiveCity() != null) {
                        existingUser.setLiveCity(userUpdateDTO.getLiveCity());
                    }
                    if (userUpdateDTO.getDescription() != null) {
                        existingUser.setDescription(userUpdateDTO.getDescription());
                    }
                    if (userUpdateDTO.getImage() != null) {
                        existingUser.setImage(userUpdateDTO.getImage());
                    }

                    repository.save(existingUser);
                    return true; // Обновление успешно
                })
                .orElse(false); // Пользователь не найден
    }

    @Transactional
    public UserDeleteDTO deleteUser(Long id) {
        return repository.findById(id)
                .map(user -> {
                    repository.deleteById(id);
                    return new UserDeleteDTO(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getRoles().stream()
                                    .map(role -> new RoleDTO(role.getId(), role.getTitle()))
                                    .collect(Collectors.toSet())
                    );
                })
                .orElse(null);
    }


    public UserDTO updateUserFields(Long id, UserDTO userDTO) {
        return repository.findById(id)
                .map(existingUser -> {
                    existingUser.setBornCity(userDTO.getBornCity());
                    existingUser.setLiveCity(userDTO.getLiveCity());
                    existingUser.setDescription(userDTO.getDescription());
                    existingUser.setImage(userDTO.getImage());
                    User updatedUser = repository.save(existingUser);
                    return convertToDTO(updatedUser);
                })
                .orElse(null);
    }


    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        userDTO.setBornCity(user.getBornCity());
        userDTO.setLiveCity(user.getLiveCity());
        userDTO.setDescription(user.getDescription());
        userDTO.setImage(user.getImage());

        if (user.getRoles() != null) {
            userDTO.setRoles(user.getRoles().stream()
                    .map(role -> new RoleDTO(role.getId(), role.getTitle()))
                    .collect(Collectors.toSet()));
        }
        return userDTO;
    }


    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        user.setBornCity(userDTO.getBornCity());
        user.setLiveCity(userDTO.getLiveCity());
        user.setDescription(userDTO.getDescription());
        user.setImage(userDTO.getImage());

        if (userDTO.getRoles() != null) {
            Set<Role> roles = userDTO.getRoles().stream()
                    .map(roleDTO -> roleRepository.findById(roleDTO.getId()).orElse(null))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        return user;
    }


    @Override
    public void register(RegisterDTO registerDTO) {

        if (repository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        Role selectedRole = roleService.getRoleById(registerDTO.getRoleId());

        User newUser = new User();
        newUser.setName(registerDTO.getName());
        newUser.setEmail(registerDTO.getEmail());
        newUser.setPassword(encoder.encode(registerDTO.getPassword()));
        newUser.setActive(false);
        newUser.setRoles(Set.of(selectedRole));

        repository.save(newUser);
        emailService.sendConfirmationEmail(newUser);
    }

    @Transactional
    public boolean activateUser(String code) {
        // Получаем код подтверждения
        ConfirmationCode confirmationCode = confirmationService.getConfirmationCode(code);

        // Проверяем, существует ли код и не истек ли его срок действия
        if (confirmationCode == null) {
            return false; // Код не найден
        }

        // Получаем пользователя, связанного с кодом
        User user = confirmationCode.getUser();

        // Если код истек
        if (confirmationCode.getExpired().isBefore(LocalDateTime.now())) {
            // Удаляем код подтверждения
            confirmationService.deleteConfirmationCode(confirmationCode);

            // Если пользователь существует, удаляем его
            if (user != null) {
                repository.delete(user);
            }

            return false; // Код истек
        }

        // Активируем пользователя
        
        user.setActive(true);
        repository.save(user);

        // Удаляем использованный код подтверждения
        confirmationService.deleteConfirmationCode(confirmationCode);

        return true;
    }

    public UserDTO getUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::convertToDTO)
                .orElse(null);
    }

}