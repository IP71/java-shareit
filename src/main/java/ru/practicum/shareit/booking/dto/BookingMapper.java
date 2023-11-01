package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

/*
Класс для преобразования экземпляров Booking и BookingDto друг в друга
*/

public class BookingMapper {
    // Метод получает объект класса Booking и возвращает объект BookingDto
    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBooker().getId())
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .itemId(booking.getItem().getId())
                .item(ItemMapper.toItemDto(booking.getItem()))
                .status(booking.getStatus())
                .build();
    }

    // Метод получает объекты классов BookingDto, User и Item и возвращает объект Booking
    public static Booking toBooking(BookingDto bookingDto, User user, Item item) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .booker(user)
                .item(item)
                .status(bookingDto.getStatus())
                .build();
    }
}
