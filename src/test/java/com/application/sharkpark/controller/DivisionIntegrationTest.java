package com.application.sharkpark.controller;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.member.User;
import com.application.sharkpark.enums.member.Role;
import com.application.sharkpark.repository.DivisionRepository;
import com.application.sharkpark.repository.member.UserRepository;
import com.application.sharkpark.service.member.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DivisionIntegrationTest {
    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    private User manager;

    @BeforeEach
    void setup() {
        manager = saveUser();
    }

    @AfterEach
    void tearDown() {

    }


    @Test
    void getAllDivisions() {
        userRepository.save(new User());
        u
        divisionRepository.save(new Division())
        divisionRepository.findAll();
    }

    private User saveUser() {
        User user = new User();
        user.setRole(Role.MANAGER);
        user.setFirstname("Test");
        user.setLastname("");
        user.setEmail("test@email.com");
        user.setPassword("password");
        user.setLicensePlate("111-AAA");
        user.setRegistrationDate(LocalDateTime.now());
        userRepository.save(user);
    }


}