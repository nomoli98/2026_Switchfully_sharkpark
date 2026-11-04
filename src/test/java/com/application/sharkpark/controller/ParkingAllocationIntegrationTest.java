package com.application.sharkpark.controller;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.ParkingAllocation;
import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateParkingAllocationRequest;
import com.application.sharkpark.dto.response.ParkingAllocationResponse;
import com.application.sharkpark.enums.MembershipLevel;
import com.application.sharkpark.enums.ParkingLotCategory;
import com.application.sharkpark.enums.Role;
import com.application.sharkpark.repository.CityRepository;
import com.application.sharkpark.repository.ParkingAllocationRepository;
import com.application.sharkpark.repository.ParkingLotRepository;
import com.application.sharkpark.repository.UserRepository;
import io.restassured.RestAssured;
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
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParkingAllocationIntegrationTest {
    private static final String ERROR_PATH = "message";

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingAllocationRepository parkingAllocationRepository;

    @Autowired
    private CityRepository cityRepository;

    private CreateParkingAllocationRequest invalidAllocationRequest;
    private User member;
    private ParkingLot parkingLot;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "/parking-allocations";

        member = saveMember();
        parkingLot = saveParkingLot();
        saveParkingAllocation();
        addAllocationParkingRequest();
    }

    @AfterEach
    void tearDown() {
        clear();
        reset();
    }

    @Nested
    class CreateParkingAllocation {

        @Test
        void givenNoCredentials_thenThrowUnauthorizedException() {
            given()
                    .contentType(JSON)
                    .body(invalidAllocationRequest)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        void givenNonExistingUser_thenThrowUnauthorizedException() {
            given()
                    .auth()
                    .preemptive()
                    .basic("", "")
                    .contentType(JSON)
                    .body(invalidAllocationRequest)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        void givenExistingUserWithWrongAccess_thenThrowForbiddenException() {
            User manager = saveManager();
            given()
                    .auth()
                    .preemptive()
                    .basic(manager.getEmail(), manager.getPassword())
                    .contentType(JSON)
                    .body(invalidAllocationRequest)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
        }

        @Test
        void givenNonExistingMember_thenThrowUserNotFoundException() {
            String errorMessage = given()
                    .auth()
                    .preemptive()
                    .basic(member.getEmail(), member.getPassword())
                    .contentType(JSON)
                    .body(invalidAllocationRequest)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .extract()
                    .path(ERROR_PATH);

            assertEquals("User with id " + invalidAllocationRequest.memberId() + " doesn't exists", errorMessage);
        }


        @Test
        void givenMemberNotOwnerAndNotGoldMembership_thenThrowRequestException() {
            CreateParkingAllocationRequest invalidAllocationRequest =
                    new CreateParkingAllocationRequest(member.getUserId(), "124-11", 100);
            String errorMessage = given()
                    .auth()
                    .preemptive()
                    .basic(member.getEmail(), member.getPassword())
                    .contentType(JSON)
                    .body(invalidAllocationRequest)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .path(ERROR_PATH);

            assertEquals("This field (licensePlate) is invalid.", errorMessage);
        }

        @Test
        void givenActiveParkingAllocationWithLicensePlate_thenThrowActiveParkingAllocationExistException() {
            CreateParkingAllocationRequest invalidAllocationRequest =
                    new CreateParkingAllocationRequest(member.getUserId(), member.getLicensePlate(), parkingLot.getId());

            String errorMessage = given()
                    .auth()
                    .preemptive()
                    .basic(member.getEmail(), member.getPassword())
                    .contentType(JSON)
                    .body(invalidAllocationRequest)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .extract()
                    .path(ERROR_PATH);

            assertEquals("An ACTIVE ParkingAllocation already exist with licensePlate = " + member.getLicensePlate()
                    + " and parkingPlotId = " + parkingLot.getId(),errorMessage);
        }

        @Test
        void givenNonExistingParkingLot_thenThrowNotFoundException() {
            CreateParkingAllocationRequest invalidAllocationRequest =
                    new CreateParkingAllocationRequest(member.getUserId(), member.getLicensePlate(), 100);

            String errorMessage = given()
                    .auth()
                    .preemptive()
                    .basic(member.getEmail(), member.getPassword())
                    .contentType(JSON)
                    .body(invalidAllocationRequest)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .extract()
                    .path(ERROR_PATH);

            assertEquals("ParkingLot not found with ID = "
                    + invalidAllocationRequest.parkingLotId(), errorMessage);
        }

        @Test
        void givenFullParkingLot_thenThrowParkingLotFullException() {
            User otherMember = saveOtherMember();

            CreateParkingAllocationRequest invalidAllocationRequest =
                    new CreateParkingAllocationRequest(otherMember.getUserId(), otherMember.getLicensePlate(),
                            parkingLot.getId());

            String errorMessage = given()
                    .auth()
                    .preemptive()
                    .basic(member.getEmail(), member.getPassword())
                    .contentType(JSON)
                    .body(invalidAllocationRequest)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .extract()
                    .path(ERROR_PATH);

            assertEquals("The parking lot with ID = " + parkingLot.getId() + " is full", errorMessage);
        }

        @Test
        void givenRightData_thenParkingAllocationCreated() {
            User otherMember = saveOtherMember();
            parkingAllocationRepository.deleteAll();
            parkingLotRepository.deleteAll();
            parkingLot = saveParkingLot();

            CreateParkingAllocationRequest allocationRequest =
                    new CreateParkingAllocationRequest(otherMember.getUserId(), otherMember.getLicensePlate(),
                            parkingLot.getId());

            ParkingAllocationResponse response = given()
                    .auth()
                    .preemptive()
                    .basic(member.getEmail(), member.getPassword())
                    .contentType(JSON)
                    .body(allocationRequest)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ParkingAllocationResponse.class);

            assertThat(response.startTime()).isNotNull();
            assertThat(response.licensePlate()).isEqualTo(otherMember.getLicensePlate());
            assertThat(response.memberId()).isEqualTo(otherMember.getUserId());
            assertThat(response.parkingLotId()).isEqualTo(parkingLot.getId());
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
        User user = createMember();
        return userRepository.save(user);
    }

    private User saveOtherMember() {
        User member = createMember();
        member.setLicensePlate("AAA-BBB");
        member.setMembershipLevel(MembershipLevel.GOLD);
        member.setEmail("other@email.com");
        return userRepository.save(member);
    }

    private void addAllocationParkingRequest() {
        invalidAllocationRequest = new CreateParkingAllocationRequest(100, "", 100);
    }

    private ParkingLot saveParkingLot() {
        City city = cityRepository.save(getCity());

        return parkingLotRepository.save(new ParkingLot("Grand Place Underground",
                ParkingLotCategory.UNDERGROUND_BUILDING, 1, "Grasmarkt",
                1, 4.50, member, city));
    }

    private void saveParkingAllocation() {
        parkingAllocationRepository.save(new ParkingAllocation(
                parkingLot, member, member.getLicensePlate()));
        parkingLot.incrementOccupancy();
        parkingLotRepository.save(parkingLot);
    }

    private City getCity() {
        City city = new City();
        city.setName("City");
        city.setPostalCode(1120);

        return city;
    }

    private User createMember() {
        User member = new User();
        member.setRole(Role.MEMBER);
        member.setFirstname("Test");
        member.setLastname("Lastname");
        member.setEmail("member@email.com");
        member.setPassword("password");
        member.setLicensePlate("1112-AAA");
        member.setRegistrationDate(LocalDate.now());
        member.setMembershipLevel(MembershipLevel.BRONZE);

        return member;
    }

    private void clear() {
        parkingAllocationRepository.deleteAll();
        parkingLotRepository.deleteAll();
        userRepository.deleteAll();
        cityRepository.deleteAll();
    }

}