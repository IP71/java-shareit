package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * Класс для преобразования экземпляров ItemRequest и ItemRequestDto друг в друга
 */

public class ItemRequestMapper {
    /**
     * Метод получает объект класса ItemRequest и возвращает объект ItemRequestDto
     *
     * @param itemRequest - объект ItemRequest, который нужно преобразовать в ItemRequestDto
     * @return - возвращает ItemRequestDto
     */
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestor().getId())
                .created(itemRequest.getCreated())
                .build();
    }

    /**
     * Метод получает объекты классов ItemRequestDto и User и возвращает объект ItemRequestDto
     *
     * @param itemRequestDto - объект ItemRequestDto, который нужно преобразовать в ItemRequest
     * @param user           - объект User, автор реквеста
     * @return - возвращает ItemRequest
     */
    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(user)
                .created(itemRequestDto.getCreated())
                .build();
    }
}
