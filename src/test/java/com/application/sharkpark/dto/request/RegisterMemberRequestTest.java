package com.application.sharkpark.dto.request;

import com.application.sharkpark.enums.MembershipLevel;
import com.application.sharkpark.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RegisterMemberRequestTest {

    @Test
    @DisplayName("holds every value as given")
    void holdsAllValues() {
        LocalDate date = LocalDate.of(2025, 3, 1);

        RegisterMemberRequest request = new RegisterMemberRequest("Jane", "Doe", "secret123",
                                                                  "+32470123456", "021234567",
                                                                  "jane.doe@example.com", "Main Street",
                                                                  12, 1000, "1-ABC-123", date,
                                                                  Role.MEMBER, MembershipLevel.SILVER);

        assertEquals("Jane", request.firstname());
        assertEquals("Doe", request.lastname());
        assertEquals("secret123", request.password());
        assertEquals("+32470123456", request.mobileNumber());
        assertEquals("021234567", request.phoneNumber());
        assertEquals("jane.doe@example.com", request.email());
        assertEquals("Main Street", request.addressStreet());
        assertEquals(12, request.addressNumber());
        assertEquals(1000, request.postalCode());
        assertEquals("1-ABC-123", request.licensePlate());
        assertEquals(date, request.registrationDate());
        assertEquals(Role.MEMBER, request.role());
        assertEquals(MembershipLevel.SILVER, request.membershipLevel());
    }

    @Test
    @DisplayName("has no defaults: null optional fields stay null (defaults are applied by CreateUserRequest)")
    void nullOptionalFields_stayNull() {
        RegisterMemberRequest request = new RegisterMemberRequest("Jane", "Doe", "secret123",
                                                                  "+32470123456", null,
                                                                  "jane.doe@example.com", null, null,
                                                                  1000, "1-ABC-123", null, null, null);

        assertNull(request.registrationDate());
        assertNull(request.role());
        assertNull(request.membershipLevel());
    }
}