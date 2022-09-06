package ru.practicum.explorewithme.user.exception;

public class UserNotActivatedException extends RuntimeException {
    public UserNotActivatedException(long userId) {
        super(String.format("User with id %d not activated", userId));
    }
}
