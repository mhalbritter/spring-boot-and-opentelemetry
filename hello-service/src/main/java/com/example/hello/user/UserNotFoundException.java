package com.example.hello.user;

public class UserNotFoundException extends RuntimeException {
    UserNotFoundException(long id, Throwable cause) {
        super("User with id %d not found".formatted(id), cause);
    }
}
