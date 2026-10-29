package com.application.sharkpark.dto.response.member;

import com.application.sharkpark.enums.member.Role;

import java.time.LocalDate;

public record UserResponse(Integer userId,
                           String firstname,
                           String lastname,
                           String password,
                           String mobileNumber,
                           String phoneNumber,
                           String email,
                           String addressStreet,
                           Integer addressNumber,
                           CityResponse city,
                           String licensePlate,
                           LocalDate registrationDate,
                           Role role) {
}
