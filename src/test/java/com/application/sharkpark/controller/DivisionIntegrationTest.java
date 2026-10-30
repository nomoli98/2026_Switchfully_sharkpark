package com.application.sharkpark.controller;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.response.DivisionResponse;
import com.application.sharkpark.enums.MembershipLevel;
import com.application.sharkpark.enums.Role;
import com.application.sharkpark.repository.DivisionRepository;
import com.application.sharkpark.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.reset;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DivisionIntegrationTest {
    private static final String BASE_PATH = "/divisions";
    private static final String ERROR_PATH = "message";

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        divisionRepository.deleteAll();
        userRepository.deleteAll();
        reset();
    }

    @Test
    void getAllDivisions_givenNoAuthorization() {
        String errorMessage = given()
                .when()
                .get(BASE_PATH)
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract()
                .path(ERROR_PATH);

        assertThat(errorMessage).isEqualTo("Invalid credentials");
    }

    @Test
    void getAllDivisions_givenInvalidUser() {
        String errorMessage = given()
                .auth()
                .preemptive()
                .basic("", "")
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_PATH)
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract()
                .path(ERROR_PATH);

        assertThat(errorMessage).isEqualTo("Invalid credentials");
    }

    @Test
    void getAllDivisions_givenExistingUserWithWrongAccess() {
        User member = saveMember();
        String errorMessage = given()
                .auth()
                .preemptive()
                .basic(member.getEmail(), member.getPassword())
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_PATH)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .path(ERROR_PATH);

        assertThat(errorMessage).isEqualTo("Access to the resource not allowed");
    }

    @Test
    void getAllDivisions_givenRightManager() {
        User manager = saveManager();
        saveDivision(manager);

        DivisionResponse[] divisionResponses = given()
                .auth()
                .preemptive()
                .basic(manager.getEmail(), manager.getPassword())
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_PATH)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DivisionResponse[].class);

        assertThat(divisionResponses).hasSize(1)
                        .extracting(DivisionResponse::name)
                .containsOnly("name");

        assertThat(divisionResponses)
                .extracting(DivisionResponse::directorName)
                        .containsOnly(String.join(" ",manager.getFirstname(),
                                manager.getLastname()));

    }



    private User saveManager() {
        User user = new User();
        user.setRole(Role.MANAGER);
        user.setFirstname("Test");
        user.setLastname("");
        user.setEmail("test@email.com");
        user.setPassword("password");
        user.setLicensePlate("111-AAA");
        user.setRegistrationDate(LocalDate.now());
        user.setMembershipLevel(MembershipLevel.BRONZE);
        return userRepository.save(user);
    }

    private User saveMember() {
        User user = new User();
        user.setRole(Role.MEMBER);
        user.setFirstname("Test");
        user.setLastname("");
        user.setEmail("member@email.com");
        user.setPassword("password");
        user.setLicensePlate("1112-AAA");
        user.setRegistrationDate(LocalDate.now());
        user.setMembershipLevel(MembershipLevel.BRONZE);
        return userRepository.save(user);
    }

    private void saveDivision(User director) {
        divisionRepository.save(new Division("name", "originalName", director));
    }


}