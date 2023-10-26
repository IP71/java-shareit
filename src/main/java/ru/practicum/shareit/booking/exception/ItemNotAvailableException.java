package ru.practicum.shareit.booking.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(long id) {
        super(String.format("Вещь с id=%d недоступна", id));
        log.error("Вещь с id={} недоступна", id);
    }
}
