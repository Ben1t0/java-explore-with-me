package ru.practicum.explorewithme.event.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long eventId) {
        super(String.format("Event with id %d not found", eventId));
    }
}
