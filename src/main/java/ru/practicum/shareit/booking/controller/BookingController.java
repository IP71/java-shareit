package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/*
Контроллер для взаимодействия с вещами
Все методы получают id пользователя из заголовка "X-Sharer-User-Id"
*/

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    // Метод создает новое бронирование при запросе POST /bookings
    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.create(bookingDto, userId);
    }

    // Метод устанавливает статус APPROVED или REJECTED для существующего бронирования при запросе PATCH /bookings/{bookingId}
    @PatchMapping("/{bookingId}")
    public BookingDto setStatus(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId,
                                @RequestParam boolean approved) {
        return bookingService.setStatus(userId, bookingId, approved);
    }

    // Метод возвращает бронирование по id при запросе GET /bookings/{bookingId}
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    // Метод возвращает список бронирований пользователя по их типу state при запросе GET /bookings
    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(required = false) State state) {
        if (state == null) {
            state = State.ALL;
        }
        return bookingService.getAllBookingsByUser(userId, state);
    }

    // Метод возвращает список бронирований для вещей, принадлежащих пользователю, по их типу state при запросе GET /bookings/owner
    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForItemsBelongToUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                               @RequestParam(required = false) State state) {
        if (state == null) {
            state = State.ALL;
        }
        return bookingService.getAllBookingsForItemsBelongToUser(userId, state);
    }
}
