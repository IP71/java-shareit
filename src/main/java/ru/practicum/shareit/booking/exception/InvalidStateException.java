package ru.practicum.shareit.booking.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidStateException extends RuntimeException {
    public InvalidStateException(String state) {
        super(String.format("Unknown state: %s", state));
        log.error("Было передано некорректное state:{}", state);
    }
}
