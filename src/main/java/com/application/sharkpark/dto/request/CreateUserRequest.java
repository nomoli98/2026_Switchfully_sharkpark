package com.application.sharkpark.dto.request;

import com.application.sharkpark.enums.MembershipLevel;
import com.application.sharkpark.enums.Role;

import java.time.LocalDate;

public record CreateUserRequest(String firstname,
                                String lastname,
                                String password,
                                String mobileNumber,
                                String phoneNumber,
                                String email,
                                String addressStreet,
                                Integer addressNumber,
                                Integer postalCode,
                                String licensePlate,
                                LocalDate registrationDate,
                                Role role,
                                MembershipLevel membershipLevel) {

    public CreateUserRequest {
        if (role == null) role = Role.MEMBER;
        if (registrationDate == null) registrationDate = LocalDate.now();
        if (membershipLevel == null) membershipLevel = MembershipLevel.BRONZE;
    }
}
