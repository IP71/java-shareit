package ru.practicum.shareit.booking.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IllegalAccessExceptionBooking extends RuntimeException {
    public IllegalAccessExceptionBooking(long userId) {
        super(String.format("Пользователь с id=%d не владелец вещи и не автор бронирования", userId));
        log.error("Пользователь с id={} не владелец вещи и не автор бронирования", userId);
    }
}
