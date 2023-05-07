package ru.practicum.ewmservice.exception;

import lombok.Getter;

@Getter
public class IncorrectlyException extends RuntimeException {

    private final String parameter;

    public IncorrectlyException(String parameter) {
        this.parameter = parameter;
    }
}
