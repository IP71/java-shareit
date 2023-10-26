package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByEndDesc(Long userId);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(Long userId, LocalDateTime date1, LocalDateTime date2);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByEndDesc(Long userId, LocalDateTime date);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByEndDesc(Long userId, LocalDateTime date);

    List<Booking> findAllByBookerIdAndStatusOrderByEndDesc(Long userId, Status status);

    List<Booking> findAllByItemOwnerOrderByEndDesc(Long userId);

    List<Booking> findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByEndDesc(Long userId, LocalDateTime date1, LocalDateTime date2);

    List<Booking> findAllByItemOwnerAndEndIsBeforeOrderByEndDesc(Long userId, LocalDateTime date);

    List<Booking> findAllByItemOwnerAndStartIsAfterOrderByEndDesc(Long userId, LocalDateTime date);

    List<Booking> findAllByItemOwnerAndStatusOrderByEndDesc(Long userId, Status status);

    Optional<Booking> findFirst1ByItemIdAndStartIsBeforeAndStatusIsNotOrderByEndDesc(Long itemId, LocalDateTime date, Status status);

    Optional<Booking> findFirst1ByItemIdAndStartIsAfterAndStatusIsNotOrderByStartAsc(Long itemId, LocalDateTime date, Status status);
}
