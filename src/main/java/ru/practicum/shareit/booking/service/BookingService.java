package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

/*
Интерфейс сервисного класса для взаимодействия с бронированиями
create(BookingDto, Long) создает новое бронирование
setStatus(Long, Long, boolean) меняет статус для существующего бронирования
getBookingById(Long, Long) возвращает бронирование по id
getAllBookingsByUser(Long, String) возвращает все бронирования пользователя по их типу (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
getAllBookingsForItemsBelongToUser(Long, String) возвращает бронирования для вещей пользователя по их типу
*/

public interface BookingService {
    BookingDto create(BookingDto bookingDto, Long userId);

    BookingDto setStatus(Long userId, Long bookingId, boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getAllBookingsByUser(Long userId, String state);

    List<BookingDto> getAllBookingsForItemsBelongToUser(Long userId, String state);
}
