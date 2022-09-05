package ru.practicum.explorewithme.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super(String.format("User with id %d not found", userId));
    }
}
