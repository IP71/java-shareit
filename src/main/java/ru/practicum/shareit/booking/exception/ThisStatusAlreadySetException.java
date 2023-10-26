package ru.practicum.shareit.booking.exception;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Status;

@Slf4j
public class ThisStatusAlreadySetException extends RuntimeException {
    public ThisStatusAlreadySetException(Status status) {
        super("Статус " + status + " уже установлен");
        log.error("Статус {} уже установлен", status);
    }
}
