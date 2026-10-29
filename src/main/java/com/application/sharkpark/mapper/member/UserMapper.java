package com.application.sharkpark.mapper.member;

import com.application.sharkpark.domain.member.City;
import com.application.sharkpark.domain.member.User;
import com.application.sharkpark.dto.request.member.CreateUserRequest;
import com.application.sharkpark.dto.request.member.RegisterMemberRequest;
import com.application.sharkpark.dto.response.member.UserResponse;
import com.application.sharkpark.enums.member.Role;
import com.application.sharkpark.validator.UserRequestValidator;

import java.time.LocalDate;
import java.util.List;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(CreateUserRequest request, City city) {
        User user = new User();
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setPassword(request.password());
        user.setMobileNumber(UserRequestValidator.normalizePhoneNumber(request.mobileNumber()));
        user.setPhoneNumber(UserRequestValidator.normalizePhoneNumber(request.phoneNumber()));
        user.setEmail(request.email());
        user.setAddressStreet(request.addressStreet());
        user.setAddressNumber(request.addressNumber());
        user.setCity(city);
        user.setLicensePlate(request.licensePlate());
        user.setRegistrationDate(request.registrationDate() != null
                                 ? request.registrationDate()
                                 : LocalDate.now());
        user.setRole(request.role() != null ? request.role() : Role.MEMBER);
        return user;
    }

    public static CreateUserRequest toCreateUserRequest(RegisterMemberRequest request) {
        return new CreateUserRequest(request.firstname(),
                                     request.lastname(),
                                     request.password(),
                                     request.mobileNumber(),
                                     request.phoneNumber(),
                                     request.email(),
                                     request.addressStreet(),
                                     request.addressNumber(),
                                     request.postalCode(),
                                     request.licensePlate(),
                                     LocalDate.now(),
                                     Role.MEMBER);
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getUserId(),
                                user.getFirstname(),
                                user.getLastname(),
                                user.getMobileNumber(),
                                user.getPhoneNumber(),
                                user.getEmail(),
                                user.getLicensePlate(),
                                user.getRegistrationDate());
    }

    public static List<UserResponse> toListOfUserResponses(List<User> users) {
        return users.stream().map(UserMapper::toUserResponse).toList();
    }
}