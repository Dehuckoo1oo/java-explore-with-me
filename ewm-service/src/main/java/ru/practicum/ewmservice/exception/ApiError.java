package ru.practicum.ewmservice.exception;

import lombok.Getter;

import java.time.LocalDateTime;

public class ApiError {

    @Getter
    private final ErrorStatus status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;

    public ApiError(ErrorStatus status, String reason, String message, LocalDateTime timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }
}
