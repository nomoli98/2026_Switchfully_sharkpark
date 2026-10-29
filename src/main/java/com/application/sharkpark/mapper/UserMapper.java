package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.member.City;
import com.application.sharkpark.domain.member.User;
import com.application.sharkpark.dto.request.member.CreateUserRequest;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(CreateUserRequest request, City city) {
        User user = new User();
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setMobileNumber(request.mobileNumber());
        user.setPhoneNumber(request.phoneNumber());
        user.setEmail(request.email());
        user.setAddressStreet(request.addressStreet());
        user.setAddressNumber(request.addressNumber());
        user.setCity(city);
        user.setLicensePlate(request.licensePlate());
        user.setRegistrationDate(request.registrationDate());
        user.setRole(request.role());
        return user;
    }
}