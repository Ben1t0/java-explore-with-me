package ru.practicum.explorewithme.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.compilation.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.event.exception.EventBadRequestException;
import ru.practicum.explorewithme.event.exception.EventDateToEarlyException;
import ru.practicum.explorewithme.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.partisipationrequest.exception.RequestNotFoundException;
import ru.practicum.explorewithme.user.exception.UserAlreadyExistsException;
import ru.practicum.explorewithme.user.exception.UserNotActivatedException;
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


    @ExceptionHandler(UserNotActivatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserInactiveException(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleAlreadyExistException(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleSqlExceptions(RuntimeException e) {
        Throwable ex = e.getCause().getCause();
        return ex.getMessage();
    }

    @ExceptionHandler({EventDateToEarlyException.class, EventBadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEventExceptions(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler({UserNotFoundException.class, CategoryNotFoundException.class, EventNotFoundException.class,
            RequestNotFoundException.class, CompilationNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(RuntimeException e) {
        return e.getMessage();
    }


    //TODO: exception ApiError

    /*@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleException(RuntimeException e){
        return null;
    }*/


    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     */
    /*@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }*/

    /**
     * Handles ConstraintViolationException. Thrown when @Validated fails.
     */
    /*@ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }*/

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     */
    /*@Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(),
                servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }*/

    /*    @ExceptionHandler(BookingUnknownStateException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, String> handleConflict(BookingUnknownStateException ex) {
            return Map.of("error", ex.getMessage());
        }*/
}
