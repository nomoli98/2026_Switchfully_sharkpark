package com.application.sharkpark.validator;

import com.application.sharkpark.dto.request.member.CreateUserRequest;
import com.application.sharkpark.exceptions.RequestException;

import java.util.regex.Pattern;

public final class UserRequestValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{8,15}$");
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

        if (isValidPhoneNumber(request.mobileNumber()))
            throw RequestException.invalidField("user mobile number");
        if (isValidPhoneNumber(request.phoneNumber()))
            throw RequestException.invalidField("user phone number");
        if (RequestValidator.isNotFilledIn(request.mobileNumber()) &&
            RequestValidator.isNotFilledIn(request.phoneNumber()))
            throw RequestException.oneOfTheTwoFieldsMustBeFilledIn("mobile number", "phone number");

        if (RequestValidator.isNotFilledIn(request.email()))
            throw RequestException.notFilledInField("user email");
        if (!isValidEmail(request.email())) throw RequestException.invalidField("user email");

        if (RequestValidator.isNegative(request.addressNumber()))
            throw RequestException.invalidField("user address number");
        if (RequestValidator.isNegative(request.postalCode()))
            throw RequestException.invalidField("user postal code");

        if (RequestValidator.isNotFilledIn(request.licensePlate()))
            throw RequestException.notFilledInField("user license plate");
    }

    static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    static boolean isValidPhoneNumber(String phoneNumber) {
        String withoutSeparators = PHONE_SEPARATORS.matcher(phoneNumber).replaceAll("");
        return PHONE_PATTERN.matcher(withoutSeparators).matches();
    }
}
