package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.IllegalAccessExceptionItem;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingDto create(BookingDto bookingDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(bookingDto.getItemId()));
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException(item.getId());
        }
        if (!bookingDto.getEnd().isAfter(bookingDto.getStart())) {
            throw new InvalidDateTimeException(bookingDto.getStart(), bookingDto.getEnd());
        }
        if (userId.equals(item.getOwner())) {
            throw new BookerAndOwnerAreTheSameException(userId, item.getId());
        }
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto, user, item));
        log.info("Бронирование с id={} было создано", booking.getId());
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto setStatus(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        if (!userId.equals(booking.getItem().getOwner())) {
            throw new IllegalAccessExceptionItem(userId, booking.getItem().getId());
        }
        if (approved) {
            if (booking.getStatus().equals(Status.APPROVED)) {
                throw new ThisStatusAlreadySetException(booking.getStatus());
            }
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        booking = bookingRepository.save(booking);
        log.info("Для бронирования с id={} установлен статус {}", booking.getId(), booking.getStatus());
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner())) {
            throw new IllegalAccessExceptionBooking(userId);
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        List<Booking> result;
        LocalDateTime date = LocalDateTime.now();
        switch (state) {
            case "ALL":
                result = bookingRepository.findAllByBookerIdOrderByEndDesc(userId);
                break;
            case "CURRENT":
                result = bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(userId, date, date);
                break;
            case "PAST":
                result = bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByEndDesc(userId, date);
                break;
            case "FUTURE":
                result = bookingRepository.findAllByBookerIdAndStartIsAfterOrderByEndDesc(userId, date);
                break;
            case "WAITING":
                result = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(userId, Status.WAITING);
                break;
            case "REJECTED":
                result = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(userId, Status.REJECTED);
                break;
            default:
                throw new InvalidStateException(state);
        }
        return result.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingsForItemsBelongToUser(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        List<Booking> result;
        LocalDateTime date = LocalDateTime.now();
        switch (state) {
            case "ALL":
                result = bookingRepository.findAllByItemOwnerOrderByEndDesc(userId);
                break;
            case "CURRENT":
                result = bookingRepository.findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByEndDesc(userId, date, date);
                break;
            case "PAST":
                result = bookingRepository.findAllByItemOwnerAndEndIsBeforeOrderByEndDesc(userId, date);
                break;
            case "FUTURE":
                result = bookingRepository.findAllByItemOwnerAndStartIsAfterOrderByEndDesc(userId, date);
                break;
            case "WAITING":
                result = bookingRepository.findAllByItemOwnerAndStatusOrderByEndDesc(userId, Status.WAITING);
                break;
            case "REJECTED":
                result = bookingRepository.findAllByItemOwnerAndStatusOrderByEndDesc(userId, Status.REJECTED);
                break;
            default:
                throw new InvalidStateException(state);
        }
        return result.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}