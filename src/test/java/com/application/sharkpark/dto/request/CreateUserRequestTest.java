package com.application.sharkpark.dto.request;

import com.application.sharkpark.enums.MembershipLevel;
import com.application.sharkpark.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserRequestTest {

    private static CreateUserRequest request(LocalDate registrationDate, Role role, MembershipLevel level) {
        return new CreateUserRequest("Jane", "Doe", "secret123", "+32470123456", "021234567",
                                     "jane.doe@example.com", "Main Street", 12, 1000, "1-ABC-123",
                                     registrationDate, role, level);
    }

    @Test
    @DisplayName("null role -> defaults to MEMBER")
    void nullRole_defaultsToMember() {
        assertEquals(Role.MEMBER, request(LocalDate.now(), null, MembershipLevel.GOLD).role());
    }

    @Test
    @DisplayName("null membership level -> defaults to BRONZE")
    void nullMembershipLevel_defaultsToBronze() {
        assertEquals(MembershipLevel.BRONZE, request(LocalDate.now(), Role.MEMBER, null).membershipLevel());
    }

    @Test
    @DisplayName("null registration date -> defaults to today")
    void nullRegistrationDate_defaultsToToday() {
        assertEquals(LocalDate.now(), request(null, Role.MEMBER, MembershipLevel.BRONZE).registrationDate());
    }

    @Test
    @DisplayName("all optional fields null -> all defaults applied")
    void allOptionalFieldsNull_allDefaultsApplied() {
        CreateUserRequest request = request(null, null, null);

        assertEquals(LocalDate.now(), request.registrationDate());
        assertEquals(Role.MEMBER, request.role());
        assertEquals(MembershipLevel.BRONZE, request.membershipLevel());
    }

    @Test
    @DisplayName("provided optional values -> kept as given, not overwritten by defaults")
    void providedOptionalValues_areKept() {
        LocalDate date = LocalDate.of(2025, 1, 15);

        CreateUserRequest request = request(date, Role.ADMIN, MembershipLevel.GOLD);

        assertEquals(date, request.registrationDate());
        assertEquals(Role.ADMIN, request.role());
        assertEquals(MembershipLevel.GOLD, request.membershipLevel());
    }

    @Test
    @DisplayName("required fields -> passed through unchanged (no trimming or normalizing)")
    void requiredFields_arePassedThroughUnchanged() {
        CreateUserRequest request = new CreateUserRequest(" Jane ", "Doe", "pw", "0470 12 34 56", null,
                                                          "jane.doe@example.com", null, null, 1000,
                                                          "1-ABC-123", null, null, null);

        assertEquals(" Jane ", request.firstname());
        assertEquals("0470 12 34 56", request.mobileNumber());
        assertNull(request.phoneNumber());
        assertNull(request.addressStreet());
        assertNull(request.addressNumber());
        assertEquals(1000, request.postalCode());
    }

    @Test
    @DisplayName("two requests with the same values -> equal (record semantics)")
    void sameValues_areEqual() {
        LocalDate date = LocalDate.of(2025, 1, 15);

        assertEquals(request(date, Role.MEMBER, MembershipLevel.SILVER),
                     request(date, Role.MEMBER, MembershipLevel.SILVER));
    }

    @Test
    @DisplayName("explicit BRONZE and null membership -> equal after defaulting")
    void explicitDefaultAndNull_areEqual() {
        LocalDate date = LocalDate.of(2025, 1, 15);

        assertEquals(request(date, Role.MEMBER, MembershipLevel.BRONZE),
                     request(date, null, null));
    }
}