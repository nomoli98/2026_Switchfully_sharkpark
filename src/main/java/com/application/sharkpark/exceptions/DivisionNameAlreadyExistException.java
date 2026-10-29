package com.application.sharkpark.exceptions;

public class DivisionNameAlreadyExistException  extends RuntimeException{
    public DivisionNameAlreadyExistException(String divisionName){
        super("Division with name " + divisionName + " already exists");
    }
}
