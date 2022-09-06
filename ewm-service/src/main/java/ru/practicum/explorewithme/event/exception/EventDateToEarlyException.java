package ru.practicum.explorewithme.event.exception;

public class EventDateToEarlyException extends RuntimeException {
    public EventDateToEarlyException() {
        super("Event starts in less than 2 hours");
    }
}
