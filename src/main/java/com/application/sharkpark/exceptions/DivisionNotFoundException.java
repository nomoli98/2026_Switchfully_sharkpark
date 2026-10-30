package com.application.sharkpark.exceptions;

public class DivisionNotFoundException extends NotFoundException {
    public DivisionNotFoundException(Integer divisionId) {
        super("Division with id " + divisionId + " does not exist");
    }
}
