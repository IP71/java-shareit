package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByEndDesc(Long userId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(Long userId, LocalDateTime date1, LocalDateTime date2, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByEndDesc(Long userId, LocalDateTime date);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByEndDesc(Long userId, LocalDateTime date, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByEndDesc(Long userId, LocalDateTime date, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusOrderByEndDesc(Long userId, Status status, Pageable pageable);

    List<Booking> findAllByItemOwnerOrderByEndDesc(Long userId, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByEndDesc(Long userId, LocalDateTime date1, LocalDateTime date2, Pageable pageable);

    List<Booking> findAllByItemOwnerAndEndIsBeforeOrderByEndDesc(Long userId, LocalDateTime date, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartIsAfterOrderByEndDesc(Long userId, LocalDateTime date, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStatusOrderByEndDesc(Long userId, Status status, Pageable pageable);

    Optional<Booking> findFirst1ByItemIdAndStartIsBeforeAndStatusIsNotOrderByEndDesc(Long itemId, LocalDateTime date, Status status);

    Optional<Booking> findFirst1ByItemIdAndStartIsAfterAndStatusIsNotOrderByStartAsc(Long itemId, LocalDateTime date, Status status);
}
