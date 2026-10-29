package com.application.sharkpark.validator;

public class RequestValidator {

    public static boolean isNotFilledIn(String string){
        return string == null || string.isBlank();
    }

    public static boolean isNotFilledIn(Object value) {
        return value == null || (value instanceof Double number && number.isNaN());
    }

    public static boolean isNegative(Number number) {
        return number != null && number.doubleValue() < 0;
    }

}
