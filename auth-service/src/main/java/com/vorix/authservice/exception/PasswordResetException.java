package com.vorix.authservice.exception;

public class PasswordResetException extends RuntimeException {
    public PasswordResetException(String message) {

        super(message);
    }
}
