package ru.practicum.ewmservice.exception;

import lombok.Getter;

@Getter
public class IncorrectlyException extends RuntimeException {

    public IncorrectlyException(String message) {
        super(message);
    }
}
