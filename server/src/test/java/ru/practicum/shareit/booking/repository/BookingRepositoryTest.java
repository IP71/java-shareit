package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/DBFiller.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/DBCleaner.sql")
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    private PageRequest pageRequest = PageRequest.of(0, 10);
    private LocalDateTime date = LocalDateTime.now();

    @Test
    public void findAllByBookerIdOrderByEndDescTest() {
        List<Booking> result = bookingRepository.findAllByBookerIdOrderByEndDesc(1L, pageRequest);
        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getItem().getId());
    }

    @Test
    public void findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDescTest() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(2L,
                date.minusHours(1), date.plusHours(1), pageRequest);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getItem().getId());
    }

    @Test
    public void findAllByBookerIdAndEndIsBeforeOrderByEndDescTest() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByEndDesc(2L, date, pageRequest);
        assertEquals(0, result.size());
    }

    @Test
    public void findAllByBookerIdAndStartIsAfterOrderByEndDescTest() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStartIsAfterOrderByEndDesc(3L, date, pageRequest);
        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getItem().getId());
    }

    @Test
    public void findAllByBookerIdAndStatusOrderByEndDescTest() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(3L, Status.WAITING, pageRequest);
        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getItem().getId());
    }

    @Test
    public void findAllByItemOwnerOrderByEndDescTest() {
        List<Booking> result = bookingRepository.findAllByItemOwnerOrderByEndDesc(2L, pageRequest);
        assertEquals(2, result.size());
        assertEquals(3L, result.get(0).getItem().getId());
        assertEquals(3L, result.get(1).getItem().getId());
    }

    @Test
    public void findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByEndDescTest() {
        List<Booking> result = bookingRepository.findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByEndDesc(1L, date, date, pageRequest);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getItem().getId());
    }

    @Test
    public void findAllByItemOwnerAndEndIsBeforeOrderByEndDescTest() {
        List<Booking> result = bookingRepository.findAllByItemOwnerAndEndIsBeforeOrderByEndDesc(1L, date, pageRequest);
        assertEquals(0, result.size());
    }

    @Test
    public void findAllByItemOwnerAndStartIsAfterOrderByEndDescTest() {
        List<Booking> result = bookingRepository.findAllByItemOwnerAndStartIsAfterOrderByEndDesc(2L, date, pageRequest);
        assertEquals(2, result.size());
    }

    @Test
    public void findAllByItemOwnerAndStatusOrderByEndDescTest() {
        List<Booking> result = bookingRepository.findAllByItemOwnerAndStatusOrderByEndDesc(2L, Status.APPROVED, pageRequest);
        assertEquals(0, result.size());
    }

    @Test
    public void findFirst1ByItemIdAndStartIsBeforeAndStatusIsNotOrderByEndDescTest() {
        Optional<Booking> booking = bookingRepository.findFirst1ByItemIdAndStartIsBeforeAndStatusIsNotOrderByEndDesc(3L, date, Status.REJECTED);
        assertTrue(booking.isEmpty());
    }

    @Test
    public void findFirst1ByItemIdAndStartIsAfterAndStatusIsNotOrderByStartAscTest() {
        Optional<Booking> booking = bookingRepository.findFirst1ByItemIdAndStartIsAfterAndStatusIsNotOrderByStartAsc(3L, date, Status.REJECTED);
        assertTrue(booking.isPresent());
    }
}
