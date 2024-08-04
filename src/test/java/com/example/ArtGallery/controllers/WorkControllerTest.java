package com.example.ArtGallery.controllers;

import com.example.ArtGallery.domain.DTO.WorkDTO;
import com.example.ArtGallery.domain.entity.Role;
import com.example.ArtGallery.domain.entity.User;
import com.example.ArtGallery.repositories.RoleRepository;
import com.example.ArtGallery.repositories.UserRepository;
import com.example.ArtGallery.security.sec_dto.TokenResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WorkControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private TestRestTemplate template;
    private HttpHeaders headers;
    private WorkDTO testWork;

    private final String TEST_PRODUCT_TITLE = "Test work title";
    private final String TEST_ADMIN_NAME = "Test Admin";
    private final String TEST_USER_NAME = "Test User";
    private final String TEST_PASSWORD = "Test password";
    private final String ADMIN_ROLE_TITLE = "ADMIN";
    private final String USER_ROLE_TITLE = "USER";

    private final String URL_PREFIX = "http://localhost:";
    private final String AUTH_RESOURCE_NAME = "/auth";
    private final String WORKS_RESOURCE_NAME = "/api/works";
    private final String LOGIN_ENDPOINT = "/login";
    private final String ALL_ENDPOINT = "/all";

    // Bearer wrer897fd897bc98b7fs8df79f8adsf
    private final String BEARER_PREFIX = "Bearer ";
    private final String AUTH_HEADER_TITLE = "Authorization";
    private String adminAccessToken;
    private String userAccessToken;

    @BeforeEach
    public void setUp() {
        template = new TestRestTemplate();
        headers = new HttpHeaders();

        testWork = new WorkDTO();
        testWork.setTitle(TEST_PRODUCT_TITLE);


        BCryptPasswordEncoder encoder = null;
        Role roleAdmin;
        Role roleUser = null;

        User admin = userRepository.findByEmail(TEST_ADMIN_NAME).orElse(null);
        User user = userRepository.findByEmail(TEST_USER_NAME).orElse(null);

        if (admin == null) {
            encoder = new BCryptPasswordEncoder();
            roleAdmin = roleRepository.findByTitle(ADMIN_ROLE_TITLE).orElse(null);
            roleUser = roleRepository.findByTitle(USER_ROLE_TITLE).orElse(null);

            if (roleAdmin == null || roleUser == null) {
                throw new RuntimeException("Role admin or role user is missing in the database");
            }

            admin = new User();
            admin.setEmail(TEST_ADMIN_NAME);
            admin.setPassword(encoder.encode(TEST_PASSWORD));
            admin.setRoles(Set.of(roleAdmin, roleUser));

            userRepository.save(admin);
        }

        if (user == null) {
            encoder = encoder == null ? new BCryptPasswordEncoder() : encoder;
            roleUser = roleUser == null ?
                    roleRepository.findByTitle(USER_ROLE_TITLE).orElse(null) : roleUser;

            if (roleUser == null) {
                throw new RuntimeException("Role user is missing in the database");
            }

            user = new User();
            user.setEmail(TEST_USER_NAME);
            user.setPassword(encoder.encode(TEST_PASSWORD));
            user.setRoles(Set.of(roleUser));

            userRepository.save(user);
        }

        admin.setPassword(TEST_PASSWORD);
        admin.setRoles(null);

        user.setPassword(TEST_PASSWORD);
        user.setRoles(null);

        // http://localhost:port/auth/login
        String url = URL_PREFIX + port + AUTH_RESOURCE_NAME + LOGIN_ENDPOINT;
        HttpEntity<User> request = new HttpEntity<>(admin, headers);

        ResponseEntity<TokenResponseDto> response = template
                .exchange(url, HttpMethod.POST, request, TokenResponseDto.class);

        assertNotNull(response.getBody(), "Auth response body is empty");
        adminAccessToken = BEARER_PREFIX + response.getBody().getAccessToken();

        request = new HttpEntity<>(user, headers);

        response = template
                .exchange(url, HttpMethod.POST, request, TokenResponseDto.class);

        assertNotNull(response.getBody(), "Auth response body is empty");
        userAccessToken = BEARER_PREFIX + response.getBody().getAccessToken();
    }

    @Test
    public void positiveGettingAllProductsWithoutAuthorization() {
        String url = URL_PREFIX + port + WORKS_RESOURCE_NAME + ALL_ENDPOINT;
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<WorkDTO[]> response = template.exchange(url, HttpMethod.GET, request, WorkDTO[].class);

        // Отладочная информация
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + Arrays.toString(response.getBody()));

        // Проверка
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has unexpected status");
        assertTrue(response.hasBody(), "Response has no body");
    }




    @Test
    public void test(){}

}