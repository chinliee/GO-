package com.team4.demo.exception;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException() {}

    public DataNotFoundException(String message) {
        super(message);
    }

}