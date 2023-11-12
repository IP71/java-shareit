package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
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

    /**
     * Метод создает новое бронирование
     *
     * @param bookingDto - объект для создания бронирования
     * @param userId     - id автора бронирования
     * @return - возвращает измененный объект Booking (в виде BookingDto)
     */
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

    /**
     * Метод меняет статус для существующего бронирования (APPROVED или REJECTED)
     *
     * @param userId    - id автора бронирования
     * @param bookingId - id бронирования
     * @param approved  - статус бронирования
     * @return - возвращает измененный объект Booking (в виде BookingDto)
     */
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

    /**
     * Метод возвращает бронирование по id
     *
     * @param userId    - id пользователя
     * @param bookingId - id бронирования
     * @return - возвращает объект Booking (в виде BookingDto)
     */
    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner())) {
            throw new IllegalAccessExceptionBooking(userId);
        }
        return BookingMapper.toBookingDto(booking);
    }

    /**
     * Метод возвращает все бронирования пользователя по типу бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     *
     * @param userId - id пользователя
     * @param state  - тип бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from   - с какого бронирования начать (начиная с самого позднего по дате окончания)
     * @param size   - количество получаемых бронирований
     * @return - возвращает список бронирований
     */
    @Override
    public List<BookingDto> getAllBookingsByUser(Long userId, State state, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Booking> result;
        LocalDateTime date = LocalDateTime.now();
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByBookerIdOrderByEndDesc(userId, pageRequest);
                break;
            case CURRENT:
                result = bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(userId, date, date, pageRequest);
                break;
            case PAST:
                result = bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByEndDesc(userId, date, pageRequest);
                break;
            case FUTURE:
                result = bookingRepository.findAllByBookerIdAndStartIsAfterOrderByEndDesc(userId, date, pageRequest);
                break;
            case WAITING:
                result = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(userId, Status.WAITING, pageRequest);
                break;
            case REJECTED:
                result = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(userId, Status.REJECTED, pageRequest);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    /**
     * Метод возвращает бронирования для вещей пользователя по типу бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     *
     * @param userId - id пользователя
     * @param state  - тип бронирования (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from   - с какого бронирования начать (начиная с самого позднего по дате окончания)
     * @param size   - количество получаемых бронирований
     * @return - возвращает список бронирований
     */
    @Override
    public List<BookingDto> getAllBookingsForItemsBelongToUser(Long userId, State state, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Booking> result;
        LocalDateTime date = LocalDateTime.now();
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByItemOwnerOrderByEndDesc(userId, pageRequest);
                break;
            case CURRENT:
                result = bookingRepository.findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByEndDesc(userId, date, date, pageRequest);
                break;
            case PAST:
                result = bookingRepository.findAllByItemOwnerAndEndIsBeforeOrderByEndDesc(userId, date, pageRequest);
                break;
            case FUTURE:
                result = bookingRepository.findAllByItemOwnerAndStartIsAfterOrderByEndDesc(userId, date, pageRequest);
                break;
            case WAITING:
                result = bookingRepository.findAllByItemOwnerAndStatusOrderByEndDesc(userId, Status.WAITING, pageRequest);
                break;
            case REJECTED:
                result = bookingRepository.findAllByItemOwnerAndStatusOrderByEndDesc(userId, Status.REJECTED, pageRequest);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
