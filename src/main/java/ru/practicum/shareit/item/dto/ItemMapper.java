package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/*
Класс для преобразования экземпляров Item и ItemDto друг в друга и Item в ItemWithBookingDto
*/

public class ItemMapper {
    // Метод получает экземпляр Item и возвращает экземпляр ItemDto
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getRequest());
    }

    // Метод получает экземпляр Item и соответствующий ему экземпляр ItemDto и возвращает обновленный экземпляр Item
    // Метод используется для обновления данных экземпляра Item в соответствии с полученным из контроллера ItemDto
    public static Item toItem(ItemDto itemDto, Item item) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequest() != null) {
            item.setRequest(itemDto.getRequest());
        }
        return item;
    }

    // Метод получает экземпляр Item и два экземпляра Booking и возвращает объект ItemWithBookingDto
    public static ItemWithBookingDto toItemWithBookingDto(Item item, BookingDto lastBooking,
                                                          BookingDto nextBooking, List<CommentDto> comments) {
        return ItemWithBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }
}