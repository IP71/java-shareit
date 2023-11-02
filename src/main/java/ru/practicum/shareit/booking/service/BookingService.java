package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

/**
 * Интерфейс сервисного класса для взаимодействия с бронированиями
 */

public interface BookingService {
    /**
     * Метод создает новое бронирование
     *
     * @param bookingDto - объект для создания бронирования
     * @param userId     - id автора бронирования
     * @return - возвращает измененный объект Booking (в виде BookingDto)
     */
    BookingDto create(BookingDto bookingDto, Long userId);

    /**
     * Метод меняет статус для существующего бронирования (APPROVED или REJECTED)
     *
     * @param userId    - id автора бронирования
     * @param bookingId - id бронирования
     * @param approved  - статус бронирования
     * @return - возвращает измененный объект Booking (в виде BookingDto)
     */
    BookingDto setStatus(Long userId, Long bookingId, boolean approved);

    /**
     * Метод возвращает бронирование по id
     *
     * @param userId    - id пользователя
     * @param bookingId - id бронирования
     * @return - возвращает объект Booking (в виде BookingDto)
     */
    BookingDto getBookingById(Long userId, Long bookingId);

    /**
     * Метод возвращает все бронирования пользователя по типу бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     *
     * @param userId - id пользователя
     * @param state  - тип бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from   - с какого бронирования начать (начиная с самого позднего по дате окончания)
     * @param size   - количество получаемых бронирований
     * @return - возвращает список бронирований
     */
    List<BookingDto> getAllBookingsByUser(Long userId, State state, int from, int size);

    /**
     * Метод возвращает бронирования для вещей пользователя по типу бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     *
     * @param userId - id пользователя
     * @param state  - тип бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from   - с какого бронирования начать (начиная с самого позднего по дате окончания)
     * @param size   - количество получаемых бронирований
     * @return - возвращает список бронирований
     */
    List<BookingDto> getAllBookingsForItemsBelongToUser(Long userId, State state, int from, int size);
}
