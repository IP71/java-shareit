package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.IllegalAccessExceptionItem;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.exception.FromIsNegativeException;
import ru.practicum.shareit.request.exception.SizeIsNotPositiveException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static ru.practicum.shareit.booking.dto.BookingMapper.toBookingDto;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ItemRepository mockItemRepository;
    @Mock
    private BookingRepository mockBookingRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private final User owner = new User(1L, "owner", "owner@email.com");
    private final User booker = new User(2L, "booker", "booker@email.com");
    private Item item;
    private Booking booking;

    @BeforeEach
    public void setFields() {
        item = new Item(1L, "Item", "item", true, 1L, null);
        booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                item, booker, Status.WAITING);
    }

    @Test
    public void create_shouldCreateBooking() {
        when(mockUserRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(mockBookingRepository.save(booking)).thenReturn(booking);
        assertEquals(toBookingDto(booking), bookingService.create(toBookingDto(booking), 2L));
    }

    @Test
    public void create_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.findById(2L)).thenReturn(Optional.empty());
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> bookingService.create(toBookingDto(booking), 2L));
        assertEquals("Пользователь с id=2 не найден", e.getMessage());
    }

    @Test
    public void create_shouldThrowWhenItemNotFound() {
        when(mockUserRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(mockItemRepository.findById(1L)).thenReturn(Optional.empty());
        ItemNotFoundException e = assertThrows(ItemNotFoundException.class,
                () -> bookingService.create(toBookingDto(booking), 2L));
        assertEquals("Вещь с id=1 не найдена", e.getMessage());
    }

    @Test
    public void create_shouldThrowWhenItemIsNotAvailable() {
        item.setAvailable(false);
        when(mockUserRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item));
        ItemNotAvailableException e = assertThrows(ItemNotAvailableException.class,
                () -> bookingService.create(toBookingDto(booking), 2L));
        assertEquals("Вещь с id=1 недоступна", e.getMessage());
    }

    @Test
    public void create_shouldThrowWhenStartIsNotBeforeEnd() {
        booking.setStart(booking.getEnd());
        when(mockUserRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item));
        InvalidDateTimeException e = assertThrows(InvalidDateTimeException.class,
                () -> bookingService.create(toBookingDto(booking), 2L));
        assertEquals("Дата и время старта бронирования: " + booking.getStart()
                + " совпадают или позже с датой конца бронирования: " + booking.getEnd(), e.getMessage());
    }

    @Test
    public void create_shouldThrowWhenBookerIsOwner() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item));
        BookerAndOwnerAreTheSameException e = assertThrows(BookerAndOwnerAreTheSameException.class,
                () -> bookingService.create(toBookingDto(booking), 1L));
        assertEquals("Пользователь с id=1 - владелец вещи с id=1 и не может её забронировать", e.getMessage());
    }

    @Test
    public void setStatus_shouldSetApproved() {
        Booking approvedBooking = new Booking(1L, booking.getStart(), booking.getEnd(),
                item, booker, Status.APPROVED);
        when(mockBookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(mockBookingRepository.save(approvedBooking)).thenReturn(approvedBooking);
        assertEquals(toBookingDto(approvedBooking), bookingService.setStatus(1L, 1L, true));
    }

    @Test
    public void setStatus_shouldSetRejected() {
        Booking rejectedBooking = new Booking(1L, booking.getStart(), booking.getEnd(),
                item, booker, Status.REJECTED);
        when(mockBookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(mockBookingRepository.save(rejectedBooking)).thenReturn(rejectedBooking);
        assertEquals(toBookingDto(rejectedBooking), bookingService.setStatus(1L, 1L, false));
    }

    @Test
    public void setStatus_shouldThrowWhenBookingNotFound() {
        when(mockBookingRepository.findById(1L)).thenReturn(Optional.empty());
        BookingNotFoundException e = assertThrows(BookingNotFoundException.class,
                () -> bookingService.setStatus(1L, 1L, true));
        assertEquals("Бронирование с id=1 не найдено", e.getMessage());
    }

    @Test
    public void setStatus_shouldThrowWhenUserIsNotOwner() {
        when(mockBookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        IllegalAccessExceptionItem e = assertThrows(IllegalAccessExceptionItem.class,
                () -> bookingService.setStatus(2L, 1L, true));
        assertEquals("Пользователь с id=2 не владелец вещи с id=1", e.getMessage());
    }

    @Test
    public void setStatus_shouldThrowWhenAlreadyApproved() {
        booking.setStatus(Status.APPROVED);
        when(mockBookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        ThisStatusAlreadySetException e = assertThrows(ThisStatusAlreadySetException.class,
                () -> bookingService.setStatus(1L, 1L, true));
        assertEquals("Статус APPROVED уже установлен", e.getMessage());
    }

    @Test
    public void getBookingById_shouldReturnBooking() {
        when(mockBookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        assertEquals(toBookingDto(booking), bookingService.getBookingById(1L, 1L));
    }

    @Test
    public void getBookingById_shouldThrowWhenBookingNotFound() {
        when(mockBookingRepository.findById(1L)).thenReturn(Optional.empty());
        BookingNotFoundException e = assertThrows(BookingNotFoundException.class,
                () -> bookingService.getBookingById(1L, 1L));
        assertEquals("Бронирование с id=1 не найдено", e.getMessage());
    }

    @Test
    public void getBookingById_shouldThrowWhenUserIsNotBookerOrOwner() {
        when(mockBookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        IllegalAccessExceptionBooking e = assertThrows(IllegalAccessExceptionBooking.class,
                () -> bookingService.getBookingById(3L, 1L));
        assertEquals("Пользователь с id=3 не владелец вещи и не автор бронирования", e.getMessage());
    }

    @Test
    public void getAllBookingsByUser_shouldReturnBookingsWhenStateIsAll() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByBookerIdOrderByEndDesc(any(), any())).thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsByUser(1L, State.ALL, 1, 1));
    }

    @Test
    public void getAllBookingsByUser_shouldReturnBookingsWhenStateIsCurrent() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(any(), any(), any(), any()))
                .thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsByUser(1L, State.CURRENT, 1, 1));
    }

    @Test
    public void getAllBookingsByUser_shouldReturnBookingsWhenStateIsPast() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByBookerIdAndEndIsBeforeOrderByEndDesc(any(), any(), any()))
                .thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsByUser(1L, State.PAST, 1, 1));
    }

    @Test
    public void getAllBookingsByUser_shouldReturnBookingsWhenStateIsFuture() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByBookerIdAndStartIsAfterOrderByEndDesc(any(), any(), any()))
                .thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsByUser(1L, State.FUTURE, 1, 1));
    }

    @Test
    public void getAllBookingsByUser_shouldReturnBookingsWhenStateIsWaiting() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(any(), any(), any()))
                .thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsByUser(1L, State.WAITING, 1, 1));
    }

    @Test
    public void getAllBookingsByUser_shouldReturnBookingsWhenStateIsRejected() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(any(), any(), any()))
                .thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsByUser(1L, State.REJECTED, 1, 1));
    }

    @Test
    public void getAllBookingsByUser_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.existsById(2L)).thenReturn(false);
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> bookingService.getAllBookingsByUser(2L, State.ALL, 1, 1));
        assertEquals("Пользователь с id=2 не найден", e.getMessage());
    }

    @Test
    public void getAllBookingsForItemsBelongToUser_shouldReturnBookingsWhenStateIsAll() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByItemOwnerOrderByEndDesc(any(), any())).thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsForItemsBelongToUser(1L, State.ALL, 1, 1));
    }

    @Test
    public void getAllBookingsForItemsBelongToUser_shouldReturnBookingsWhenStateIsCurrent() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByEndDesc(any(), any(), any(), any()))
                .thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsForItemsBelongToUser(1L, State.CURRENT, 1, 1));
    }

    @Test
    public void getAllBookingsForItemsBelongToUser_shouldReturnBookingsWhenStateIsPast() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByItemOwnerAndEndIsBeforeOrderByEndDesc(any(), any(), any()))
                .thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsForItemsBelongToUser(1L, State.PAST, 1, 1));
    }

    @Test
    public void getAllBookingsForItemsBelongToUser_shouldReturnBookingsWhenStateIsFuture() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByItemOwnerAndStartIsAfterOrderByEndDesc(any(), any(), any()))
                .thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsForItemsBelongToUser(1L, State.FUTURE, 1, 1));
    }

    @Test
    public void getAllBookingsForItemsBelongToUser_shouldReturnBookingsWhenStateIsWaiting() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByItemOwnerAndStatusOrderByEndDesc(any(), any(), any()))
                .thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsForItemsBelongToUser(1L, State.WAITING, 1, 1));
    }

    @Test
    public void getAllBookingsForItemsBelongToUser_shouldReturnBookingsWhenStateIsRejected() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockBookingRepository.findAllByItemOwnerAndStatusOrderByEndDesc(any(), any(), any()))
                .thenReturn(List.of(booking));
        assertEquals(List.of(toBookingDto(booking)), bookingService.getAllBookingsForItemsBelongToUser(1L, State.REJECTED, 1, 1));
    }

    @Test
    public void getAllBookingsForItemsBelongToUser_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.existsById(2L)).thenReturn(false);
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> bookingService.getAllBookingsForItemsBelongToUser(2L, State.ALL, 1, 1));
        assertEquals("Пользователь с id=2 не найден", e.getMessage());
    }

    @Test
    public void getAllBookingsForItemsBelongToUser_shouldThrowWhenFromIsNegative() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        FromIsNegativeException e = assertThrows(FromIsNegativeException.class,
                () -> bookingService.getAllBookingsForItemsBelongToUser(1L, State.ALL, -1, 1));
        assertEquals("Было передано отрицательное значение from=-1", e.getMessage());
    }

    @Test
    public void getAllBookingsForItemsBelongToUser_shouldThrowWhenSizeIsNotPositive() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        SizeIsNotPositiveException e = assertThrows(SizeIsNotPositiveException.class,
                () -> bookingService.getAllBookingsForItemsBelongToUser(1L, State.ALL, 1, 0));
        assertEquals("Было передано не положительное значение size=0", e.getMessage());
    }
}
