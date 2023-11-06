package ru.practicum.shareit.booking.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookerAndOwnerAreTheSameException extends RuntimeException {
    public BookerAndOwnerAreTheSameException(long userId, long itemId) {
        super(String.format("Пользователь с id=%d - владелец вещи с id=%d и не может её забронировать", userId, itemId));
        log.error("Пользователь с id={} - владелец вещи с id={} и не может её забронировать", userId, itemId);
    }
}
