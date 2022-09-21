package ru.practicum.explore_with_me.exception;

public class LocationBadRequestData extends RuntimeException {
    public LocationBadRequestData(String message) {
        super(message);
    }
}
