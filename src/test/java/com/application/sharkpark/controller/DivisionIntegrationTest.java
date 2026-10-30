package com.application.sharkpark.controller;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.response.DivisionResponse;
import com.application.sharkpark.enums.MembershipLevel;
import com.application.sharkpark.enums.Role;
import com.application.sharkpark.repository.DivisionRepository;
import com.application.sharkpark.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class GetAllDivisions {
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
    }

    @Nested
    class CreateSubdivision {
        @Test
        void createSubdivision_givenValidManagerAndExistingParentDivision_returnsUpdatedDivisionWithSubdivision() {
            User manager = saveManager();

            Division parentDivision = new Division("Parent Division", "Original Parent Name", manager);
            parentDivision = divisionRepository.save(parentDivision);
            int parentDivisionId = parentDivision.getDivisionId();

            CreateDivisionRequest validCreateDivisionRequest = new CreateDivisionRequest(
                    "Valid ParkShark Belgium",
                    "Belgium Parking Company",
                    manager.getUserId()
            );

            DivisionResponse response = given()
                    .auth()
                    .preemptive()
                    .basic(manager.getEmail(), manager.getPassword())
                    .contentType(ContentType.JSON)
                    .body(validCreateDivisionRequest)
                    .when()
                    .post(BASE_PATH + "/" + parentDivisionId + "/subdivisions")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(DivisionResponse.class);

            assertThat(response).isNotNull();
            assertThat(response.name()).isEqualTo("Parent Division");
            assertThat(response.originalName()).isEqualTo("Original Parent Name");
            assertThat(response.directorName()).isEqualTo(String.join(" ", manager.getFirstname(), manager.getLastname()).trim());

            assertThat(response.subdivisions())
                    .hasSize(1)
                    .allSatisfy(subdivision -> {
                        assertThat(subdivision.subdivisionId())
                                .isGreaterThan(1);
                        assertThat(subdivision.name()).
                                isEqualTo("Valid ParkShark Belgium");
                        assertThat(subdivision.originalName())
                                .isEqualTo("Belgium Parking Company");
                        assertThat(subdivision.directorName())
                                .isEqualTo(String.join(" ", manager.getFirstname(), manager.getLastname()).trim());
                    });
        }

        @Test
        void createSubdivision_givenNonExistentParentId_returnsNotFoundStatus() {
            User manager = saveManager();
            int nonExistentId = 9999;

            CreateDivisionRequest validCreateDivisionRequest = new CreateDivisionRequest(
                    "Valid ParkShark Belgium",
                    "Belgium Parking Company",
                    manager.getUserId()
            );

            String errorMessage = given()
                    .auth()
                    .preemptive()
                    .basic(manager.getEmail(), manager.getPassword())
                    .contentType(ContentType.JSON)
                    .body(validCreateDivisionRequest)
                    .when()
                    .post(BASE_PATH + "/" + nonExistentId + "/subdivisions")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .extract()
                    .path(ERROR_PATH);

            assertThat(errorMessage).isNotNull();
        }
    }



    private User saveManager() {
        User user = new User();
        user.setRole(Role.MANAGER);
        user.setFirstname("Test");
        user.setLastname("Lastname");
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
        user.setLastname("Lastname");
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