package ru.practicum.explorewithme.admin.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException() {
        super("You don't have permissions for this action");
    }
}
