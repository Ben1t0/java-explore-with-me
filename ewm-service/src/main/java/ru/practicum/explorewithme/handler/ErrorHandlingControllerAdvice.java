package ru.practicum.explorewithme.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.admin.exception.AccessDeniedException;
import ru.practicum.explorewithme.user.exception.UserAlreadyExistsException;
import ru.practicum.explorewithme.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.validation.ValidationErrorResponse;
import ru.practicum.explorewithme.validation.Violation;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

/*    @ExceptionHandler(BookingUnknownStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConflict(BookingUnknownStateException ex) {
        return Map.of("error", ex.getMessage());
    }*/

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(RuntimeException e) {
        return e.getMessage();
    }

/*
    @ExceptionHandler(ItemUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleItemUnavailableException(RuntimeException e) {
        return e.getMessage();
    }*/

    @ExceptionHandler({UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleAlreadyExistException(RuntimeException e) {
        return e.getMessage();
    }


    /*@ExceptionHandler({WrongBookingTimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCustomRuntimeExceptions(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BookingAlreadyChecked.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBookingAlreadyCheckedException(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler(CommentNoBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCommentNoBookingException(RuntimeException e) {
        return e.getMessage();
    }*/
}
