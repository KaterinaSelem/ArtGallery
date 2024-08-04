package com.example.ArtGallery.service;

import com.example.ArtGallery.domain.DTO.RegisterDTO;
import com.example.ArtGallery.domain.DTO.RoleDTO;
import com.example.ArtGallery.domain.DTO.UserDTO;
import com.example.ArtGallery.domain.DTO.UserDeleteDTO;
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

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, RoleServiceImpl roleService, EmailService emailService) {
        this.repository = userRepository;
        this.encoder = encoder;
        this.roleService = roleService;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return repository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("User %s not found", username)));
    }

    @Autowired
    private RoleRepository roleRepository;

    public List<UserDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
                    Role role = roleRepository.findById(userDTO.getUserRole().getId()).orElse(null);
                    existingUser.setUserRole(role);
                    existingUser.setBornCity(userDTO.getBornCity());
                    existingUser.setLiveCity(userDTO.getLiveCity());
                    existingUser.setDescription(userDTO.getDescription());
                    existingUser.setImage(userDTO.getImage());
                    User updatedUser = repository.save(existingUser);
                    return convertToDTO(updatedUser);
                })
                .orElse(null);
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
                            new RoleDTO(
                                    user.getUserRole().getId(),
                                    user.getUserRole().getTitle()
                            )
                    );
                })
                .orElse(null);
    }

    public UserDTO registerUser(RegisterDTO registerDTO) {
        // Получаем роль по идентификатору
        Role selectedRole = roleRepository.findById(registerDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Создаем нового пользователя и присваиваем ему выбранную роль
        User newUser = new User();
        newUser.setName(registerDTO.getName());
        newUser.setEmail(registerDTO.getEmail());
        newUser.setPassword(registerDTO.getPassword());
        newUser.setUserRole(selectedRole);

        // Сохраняем пользователя в базе данных
        User savedUser = repository.save(newUser);
        return convertToDTO(savedUser);
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

        if (user.getUserRole() != null) {
            userDTO.setUserRole(new RoleDTO(user.getUserRole().getId(), user.getUserRole().getTitle()));
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

        if (userDTO.getUserRole() != null) {
            Role role = roleRepository.findById(userDTO.getUserRole().getId()).orElse(null);
            user.setUserRole(role);
        }
        return user;
    }

//      Recover 04.08 15:32
//    @Override
//    public void register(User user) {
//        user.setId(null);
//        user.setPassword(encoder.encode(user.getPassword()));
//        user.setActive(false);
//        user.setRoles(Set.of(roleService.getRoleUser()));
//
//        repository.save(user);
//        emailService.sendConfirmationEmail(user);
//    }

    @Override
    public void register(RegisterDTO registerDTO) {
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


}