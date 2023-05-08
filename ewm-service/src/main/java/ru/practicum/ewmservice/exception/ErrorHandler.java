package ru.practicum.ewmservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotFoundResource(final NotFoundResourceException e) {
        return new ApiError(ErrorStatus.NOT_FOUND, "Incorrectly made request.", e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundBody(final NoSuchBodyException e) {
        return new ApiError(ErrorStatus.NOT_FOUND, "The requied object was not found", e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidBody(final MethodArgumentNotValidException e) {
        return new ApiError(ErrorStatus.BAD_REQUEST, "Incorrectly made request.", e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleIncorrectly(final IncorrectlyException e) {
        return new ApiError(ErrorStatus.CONFLICT, "For the required object operation the conditions are not met",
                e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleIncorrectly(final ConflictException e) {
        return new ApiError(ErrorStatus.CONFLICT, "For the required object operation the conditions are not met",
                e.getMessage(), LocalDateTime.now());
    }
}

