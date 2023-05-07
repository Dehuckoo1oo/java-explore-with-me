package ru.practicum.ewmservice.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {

    private final String parameter;

    public ConflictException(String parameter) {
        this.parameter = parameter;
    }
}
