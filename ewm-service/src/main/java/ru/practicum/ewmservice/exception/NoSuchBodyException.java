package ru.practicum.ewmservice.exception;

import lombok.Getter;

@Getter
public class NoSuchBodyException extends RuntimeException {
    public NoSuchBodyException(String message) {
        super(message);
    }
}
