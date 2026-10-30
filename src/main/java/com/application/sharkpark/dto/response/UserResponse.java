package com.application.sharkpark.dto.response;

import com.application.sharkpark.enums.MembershipLevel;

import java.time.LocalDate;

public record UserResponse(Integer userId,
                           String firstname,
                           String lastname,
                           String mobileNumber,
                           String phoneNumber,
                           String email,
                           String licensePlate,
                           LocalDate registrationDate,
                           MembershipLevel membershipLevel) {
}
