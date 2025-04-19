package com.afric.auth.service.integration;

import com.afric.auth.service.model.Role;
import com.afric.auth.service.model.User;
import com.afric.auth.service.repository.UserRepository;
import com.afric.common.dto.request.LoginRequestDto;
import com.afric.common.dto.request.RegisterRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AuthControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_Success() throws Exception {
        // Arrange
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(
                "Test User",
                "test@example.com",
                "password123",
                null
        );

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.name").value(registerRequestDto.name()))
                .andExpect(jsonPath("$.data.email").value(registerRequestDto.email()));
    }

    @Test
    void registerUser_DuplicateEmail() throws Exception {
        // Arrange
        User existingUser = createUser("test@example.com");
        userRepository.save(existingUser);

        RegisterRequestDto registerRequestDto = new RegisterRequestDto(
                "Test User",
                "test@example.com",
                "password123",
                null
        );

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error: Email is already in use!"));
    }

    @Test
    void login_Success() throws Exception {
        // Arrange
        String password = "password123";
        User user = createUser("test@example.com");
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "test@example.com",
                password
        );

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Authentication successful"))
                .andExpect(jsonPath("$.data.email").value(loginRequestDto.email()))
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();
    }

    @Test
    void login_InvalidCredentials() throws Exception {
        // Arrange
        User user = createUser("test@example.com");
        user.setPassword(passwordEncoder.encode("correctPassword"));
        userRepository.save(user);

        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "test@example.com",
                "wrongPassword"
        );

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isUnauthorized());
    }

    private User createUser(String email) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);

        return new User(
                null,
                "Test User",
                email,
                "password",
                roles,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "SYSTEM",
                "SYSTEM"
        );
    }
}