package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * Контроллер для взаимодействия с бронированиями (Booking)
 */

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    /**
     * Метод создает новое бронирование при запросе POST /bookings
     *
     * @param bookingDto - полученный в теле запроса объект BookingDto
     * @param userId     - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @return - возвращает созданный объект Booking (в виде BookingDto)
     */
    @PostMapping
    public BookingDto create(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.create(bookingDto, userId);
    }

    /**
     * Метод устанавливает статус APPROVED или REJECTED для существующего бронирования при запросе PATCH /bookings/{bookingId}
     *
     * @param userId    - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param bookingId - id изменяемого бронирования
     * @param approved  - статус бронирования
     * @return - возвращает измененный объект Booking (в виде BookingDto)
     */
    @PatchMapping("/{bookingId}")
    public BookingDto setStatus(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId,
                                @RequestParam boolean approved) {
        return bookingService.setStatus(userId, bookingId, approved);
    }

    /**
     * Метод возвращает бронирование по id при запросе GET /bookings/{bookingId}
     *
     * @param userId    - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param bookingId - id возвращаемого бронирования
     * @return - возвращает объект BookingDto
     */
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    /**
     * Метод возвращает список бронирований пользователя по их типу state при запросе GET /bookings
     *
     * @param userId - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param state  - тип бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from   - с какого бронирования начать (начиная с самого позднего по дате окончания)
     * @param size   - количество получаемых бронирований
     * @return - возвращает список бронирований
     */
    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(required = false) State state,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "20") int size) {
        if (state == null) {
            state = State.ALL;
        }
        return bookingService.getAllBookingsByUser(userId, state, from, size);
    }

    /**
     * Метод возвращает список бронирований для вещей, принадлежащих пользователю, по их типу state при запросе GET /bookings/owner
     *
     * @param userId - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param state  - тип бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from   - с какого бронирования начать (начиная с самого позднего по дате окончания)
     * @param size   - количество получаемых бронирований
     * @return - возвращает список бронирований
     */
    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForItemsBelongToUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                               @RequestParam(required = false) State state,
                                                               @RequestParam(defaultValue = "0") int from,
                                                               @RequestParam(defaultValue = "20") int size) {
        if (state == null) {
            state = State.ALL;
        }
        return bookingService.getAllBookingsForItemsBelongToUser(userId, state, from, size);
    }
}
