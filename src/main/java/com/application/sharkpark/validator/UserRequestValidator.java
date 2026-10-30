package com.application.sharkpark.validator;

import com.application.sharkpark.dto.request.CreateUserRequest;
import com.application.sharkpark.exceptions.RequestException;

import java.util.regex.Pattern;

public final class UserRequestValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    // Max 11 digits (+ optional "+") so the normalized number fits the VARCHAR(12) columns.
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{8,11}$");
    private static final Pattern PHONE_SEPARATORS = Pattern.compile("[\\s.\\-]");

    private UserRequestValidator() {
    }

    public static void validateNewUser(CreateUserRequest request) {
        if (RequestValidator.isNotFilledIn(request.firstname()))
            throw RequestException.notFilledInField("user firstname");
        if (RequestValidator.isNotFilledIn(request.lastname()))
            throw RequestException.notFilledInField("user lastname");
        if (RequestValidator.isNotFilledIn(request.password()))
            throw RequestException.notFilledInField("user password");

        validatePhoneNumbers(request.mobileNumber(), request.phoneNumber());

        if (RequestValidator.isNotFilledIn(request.email()))
            throw RequestException.notFilledInField("user email");
        if (!isValidEmail(request.email()))
            throw RequestException.invalidField("user email");

        if (RequestValidator.isNegative(request.addressNumber()))
            throw RequestException.invalidField("user address number");
        if (RequestValidator.isNegative(request.postalCode()))
            throw RequestException.invalidField("user postal code");

        if (RequestValidator.isNotFilledIn(request.licensePlate()))
            throw RequestException.notFilledInField("user license plate");
    }

    private static void validatePhoneNumbers(String mobileNumber, String phoneNumber) {
        boolean hasMobile = !RequestValidator.isNotFilledIn(mobileNumber);
        boolean hasPhone = !RequestValidator.isNotFilledIn(phoneNumber);

        if (!hasMobile && !hasPhone)
            throw RequestException.oneOfTheTwoFieldsMustBeFilledIn("mobile number", "phone number");
        if (hasMobile && !isValidPhoneNumber(mobileNumber))
            throw RequestException.invalidField("user mobile number");
        if (hasPhone && !isValidPhoneNumber(phoneNumber))
            throw RequestException.invalidField("user phone number");
    }

    static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    static boolean isValidPhoneNumber(String phoneNumber) {
        String normalized = normalizePhoneNumber(phoneNumber);
        return normalized != null && PHONE_PATTERN.matcher(normalized).matches();
    }


    public static String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) return null;
        return PHONE_SEPARATORS.matcher(phoneNumber).replaceAll("");
    }
}