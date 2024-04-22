package org.example.gamelistv2.exception;

public class UserHasNoAccessException extends RuntimeException {

    public UserHasNoAccessException(String message) {
        super(message);
    }
}
