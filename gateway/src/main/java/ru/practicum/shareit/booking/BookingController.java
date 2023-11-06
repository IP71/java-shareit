package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Контроллер для взаимодействия с бронированиями (Booking)
 */

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    /**
     * Метод возвращает список бронирований пользователя по их типу state при запросе GET /bookings
     *
     * @param userId     - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param stateParam - тип бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from       - с какого бронирования начать (начиная с самого позднего по дате окончания)
     * @param size       - количество получаемых бронирований
     * @return - возвращает список бронирований
     */
    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get bookings with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    /**
     * Метод создает новое бронирование при запросе POST /bookings
     *
     * @param requestDto - полученный в теле запроса объект BookItemRequestDto
     * @param userId     - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @return - возвращает созданный объект Booking (в виде BookingDto)
     */
    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    /**
     * Метод возвращает бронирование по id при запросе GET /bookings/{bookingId}
     *
     * @param userId    - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param bookingId - id возвращаемого бронирования
     * @return - возвращает объект BookingDto
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
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
    public ResponseEntity<Object> setStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable long bookingId,
                                            @RequestParam(name = "approved") boolean approved) {
        log.info("Set status approved={} for booking {}, userId={}", approved, bookingId, userId);
        return bookingClient.setStatus(userId, bookingId, approved);
    }

    /**
     * Метод возвращает список бронирований для вещей, принадлежащих пользователю, по их типу state при запросе GET /bookings/owner
     *
     * @param userId     - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param stateParam - тип бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from       - с какого бронирования начать (начиная с самого позднего по дате окончания)
     * @param size       - количество получаемых бронирований
     * @return - возвращает список бронирований
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsForOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get bookings with state {}, owner={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingsForOwner(userId, state, from, size);
    }
}
