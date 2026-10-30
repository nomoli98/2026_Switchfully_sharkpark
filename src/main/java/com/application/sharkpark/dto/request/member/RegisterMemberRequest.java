package com.application.sharkpark.dto.request.member;

import com.application.sharkpark.enums.member.Role;

import java.time.LocalDate;

public record RegisterMemberRequest(String firstname,
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
                                    Role role) {
}
