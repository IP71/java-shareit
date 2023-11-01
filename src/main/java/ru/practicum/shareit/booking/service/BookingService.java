package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

/*
Интерфейс сервисного класса для взаимодействия с бронированиями
create(BookingDto, Long) создает новое бронирование
setStatus(Long, Long, boolean) меняет статус для существующего бронирования
getBookingById(Long, Long) возвращает бронирование по id
getAllBookingsByUser(Long, String, int, int) возвращает все бронирования пользователя по их типу
(ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
getAllBookingsForItemsBelongToUser(Long, String, int, int) возвращает бронирования для вещей пользователя по их типу
*/

public interface BookingService {
    BookingDto create(BookingDto bookingDto, Long userId);

    BookingDto setStatus(Long userId, Long bookingId, boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getAllBookingsByUser(Long userId, State state, int from, int size);

    List<BookingDto> getAllBookingsForItemsBelongToUser(Long userId, State state, int from, int size);
}
