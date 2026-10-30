package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateUserRequest;
import com.application.sharkpark.dto.request.RegisterMemberRequest;
import com.application.sharkpark.dto.response.UserResponse;
import com.application.sharkpark.enums.MembershipLevel;
import com.application.sharkpark.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.application.sharkpark.controller.member.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private City brussels;

    @BeforeEach
    void setUp() {
        brussels = brussels();
        brussels.setId(1);
    }

    private static RegisterMemberRequest registerRequest(LocalDate date, Role role, MembershipLevel level) {
        return new RegisterMemberRequest("Jane", "Doe", "secret123", "+32470123456", "021234567",
                                         VALID_EMAIL, "Main Street", 12, BRUSSELS_POSTAL_CODE, VALID_PLATE,
                                         date, role, level);
    }

    // ---------- toCreateUserRequest ----------

    @Test
    @DisplayName("toCreateUserRequest -> copies the personal fields")
    void toCreateUserRequest_copiesFields() {
        CreateUserRequest result = UserMapper.toCreateUserRequest(registerRequest(null, null, null));

        assertEquals("Jane", result.firstname());
        assertEquals("Doe", result.lastname());
        assertEquals("secret123", result.password());
        assertEquals("+32470123456", result.mobileNumber());
        assertEquals("021234567", result.phoneNumber());
        assertEquals(VALID_EMAIL, result.email());
        assertEquals("Main Street", result.addressStreet());
        assertEquals(12, result.addressNumber());
        assertEquals(BRUSSELS_POSTAL_CODE, result.postalCode());
        assertEquals(VALID_PLATE, result.licensePlate());
    }

    @Test
    @DisplayName("toCreateUserRequest -> role is always MEMBER, even if the request asks for ADMIN")
    void toCreateUserRequest_alwaysUsesMemberRole() {
        CreateUserRequest result = UserMapper.toCreateUserRequest(registerRequest(null, Role.ADMIN, null));

        assertEquals(Role.MEMBER, result.role());
    }

    @Test
    @DisplayName("toCreateUserRequest -> keeps the chosen membership level")
    void toCreateUserRequest_keepsMembershipLevel() {
        CreateUserRequest result = UserMapper.toCreateUserRequest(registerRequest(null, null, MembershipLevel.GOLD));

        assertEquals(MembershipLevel.GOLD, result.membershipLevel());
    }

    @Test
    @DisplayName("toCreateUserRequest without membership level -> BRONZE")
    void toCreateUserRequest_withoutMembership_defaultsToBronze() {
        CreateUserRequest result = UserMapper.toCreateUserRequest(registerRequest(null, null, null));

        assertEquals(MembershipLevel.BRONZE, result.membershipLevel());
    }

    @Test
    @DisplayName("toCreateUserRequest -> registration date is always today, a given date is ignored")
    void toCreateUserRequest_usesTodayAsRegistrationDate() {
        CreateUserRequest result = UserMapper.toCreateUserRequest(
                registerRequest(LocalDate.of(2020, 1, 1), null, null));

        assertEquals(LocalDate.now(), result.registrationDate());
    }

    // ---------- toEntity ----------

    @Test
    @DisplayName("toEntity -> copies all fields, role and membership, and links the city")
    void toEntity_copiesAllFields() {
        LocalDate date = LocalDate.of(2025, 3, 1);
        CreateUserRequest request = request().registrationDate(date)
                                             .role(Role.MANAGER)
                                             .membershipLevel(MembershipLevel.SILVER)
                                             .build();

        User user = UserMapper.toEntity(request, brussels);

        assertNull(user.getUserId());
        assertEquals("Jane", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        assertEquals("secret123", user.getPassword());
        assertEquals(VALID_EMAIL, user.getEmail());
        assertEquals("Main Street", user.getAddressStreet());
        assertEquals(12, user.getAddressNumber());
        assertSame(brussels, user.getCity());
        assertEquals(VALID_PLATE, user.getLicensePlate());
        assertEquals(date, user.getRegistrationDate());
        assertEquals(Role.MANAGER, user.getRole());
        assertEquals(MembershipLevel.SILVER, user.getMembershipLevel());
    }

    @Test
    @DisplayName("toEntity with defaulted request -> membership BRONZE, never null")
    void toEntity_withDefaults_hasNonNullMembership() {
        User user = UserMapper.toEntity(request().membershipLevel(null).role(null).build(), brussels);

        assertEquals(MembershipLevel.BRONZE, user.getMembershipLevel());
        assertEquals(Role.MEMBER, user.getRole());
        assertEquals(LocalDate.now(), user.getRegistrationDate());
    }

    @Test
    @DisplayName("toEntity -> phone numbers are normalized (spaces, dots, dashes removed)")
    void toEntity_normalizesPhoneNumbers() {
        CreateUserRequest request = request().mobileNumber("0470 12 34 56").phoneNumber("02.123-45.67").build();

        User user = UserMapper.toEntity(request, brussels);

        assertEquals("0470123456", user.getMobileNumber());
        assertEquals("021234567", user.getPhoneNumber());
    }

    @Test
    @DisplayName("toEntity -> blank phone number is stored as null")
    void toEntity_blankPhoneNumber_becomesNull() {
        User user = UserMapper.toEntity(request().phoneNumber("   ").build(), brussels);

        assertNull(user.getPhoneNumber());
    }

    // ---------- toUserResponse ----------

    @Test
    @DisplayName("toUserResponse -> copies the public fields including membership level")
    void toUserResponse_copiesFields() {
        User user = user(5, "Jane", "Doe", VALID_EMAIL, VALID_PLATE, brussels);
        user.setMembershipLevel(MembershipLevel.GOLD);

        UserResponse response = UserMapper.toUserResponse(user);

        assertEquals(5, response.userId());
        assertEquals("Jane", response.firstname());
        assertEquals("Doe", response.lastname());
        assertEquals("+32470123456", response.mobileNumber());
        assertEquals("021234567", response.phoneNumber());
        assertEquals(VALID_EMAIL, response.email());
        assertEquals(VALID_PLATE, response.licensePlate());
        assertEquals(LocalDate.now(), response.registrationDate());
        assertEquals(MembershipLevel.GOLD, response.membershipLevel());
    }

    @Test
    @DisplayName("toListOfUserResponses -> keeps size and order")
    void toListOfUserResponses_keepsOrder() {
        User jane = user(1, "Jane", "Doe", VALID_EMAIL, VALID_PLATE, brussels);
        User john = user(2, "John", "Smith", "john@example.com", "1-XYZ-999", brussels);

        List<UserResponse> responses = UserMapper.toListOfUserResponses(List.of(jane, john));

        assertEquals(List.of(1, 2), responses.stream().map(UserResponse::userId).toList());
    }

    @Test
    @DisplayName("toListOfUserResponses with empty list -> empty list")
    void toListOfUserResponses_empty() {
        assertTrue(UserMapper.toListOfUserResponses(List.of()).isEmpty());
    }
}