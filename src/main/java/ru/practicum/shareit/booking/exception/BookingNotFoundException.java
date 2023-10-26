package ru.practicum.shareit.booking.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(long id) {
        super(String.format("Бронирование с id=%d не найдено", id));
        log.error("Бронирование с id={} не найдено", id);
    }
}
