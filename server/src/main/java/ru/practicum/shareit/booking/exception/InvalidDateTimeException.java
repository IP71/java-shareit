package ru.practicum.shareit.booking.exception;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class InvalidDateTimeException extends RuntimeException {
    public InvalidDateTimeException(LocalDateTime start, LocalDateTime end) {
        super("Дата и время старта бронирования: " + start + " совпадают или позже с датой конца бронирования: " + end);
        log.error("Дата и время старта бронирования: {} совпадают или позже с датой конца бронирования: {}", start, end);
    }
}
