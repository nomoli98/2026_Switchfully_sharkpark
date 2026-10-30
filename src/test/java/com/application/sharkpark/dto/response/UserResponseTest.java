package com.application.sharkpark.dto.response;

import com.application.sharkpark.enums.MembershipLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.RecordComponent;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    private static List<String> componentNames() {
        return Arrays.stream(UserResponse.class.getRecordComponents())
                     .map(RecordComponent::getName)
                     .toList();
    }

    @Test
    @DisplayName("never exposes a password")
    void hasNoPasswordComponent() {
        assertTrue(componentNames().stream().noneMatch(name -> name.toLowerCase().contains("password")));
    }

    @Test
    @DisplayName("does not expose the role")
    void hasNoRoleComponent() {
        assertFalse(componentNames().contains("role"));
    }

    @Test
    @DisplayName("exposes the membership level")
    void exposesMembershipLevel() {
        assertTrue(componentNames().contains("membershipLevel"));
    }

    @Test
    @DisplayName("holds every value as given")
    void holdsAllValues() {
        LocalDate date = LocalDate.of(2025, 3, 1);

        UserResponse response = new UserResponse(7, "Jane", "Doe", "+32470123456", "021234567",
                                                 "jane.doe@example.com", "1-ABC-123", date,
                                                 MembershipLevel.GOLD);

        assertEquals(7, response.userId());
        assertEquals("Jane", response.firstname());
        assertEquals("Doe", response.lastname());
        assertEquals("+32470123456", response.mobileNumber());
        assertEquals("021234567", response.phoneNumber());
        assertEquals("jane.doe@example.com", response.email());
        assertEquals("1-ABC-123", response.licensePlate());
        assertEquals(date, response.registrationDate());
        assertEquals(MembershipLevel.GOLD, response.membershipLevel());
    }
}