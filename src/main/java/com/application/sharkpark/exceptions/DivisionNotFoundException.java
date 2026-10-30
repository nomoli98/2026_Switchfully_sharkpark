package com.application.sharkpark.exceptions;

public class DivisionNotFoundException extends RuntimeException {
    public DivisionNotFoundException(int divisionId) {
        super("Division not found with ID = " + divisionId);
    }
}
