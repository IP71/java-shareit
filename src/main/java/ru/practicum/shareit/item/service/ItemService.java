package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/*
Интерфейс сервисного класса для взаимодействия с вещами
create(Item, long) создает новую вещь
update(ItemDto, long) обновляет существующую вещь
get(long) возвращает список вещей пользователя
getItemById(long) возвращает вещь по id
search(String) возвращает список подходящих по параметру поиска вещей
*/

public interface ItemService {
    ItemDto create(Item item, Long ownerId);

    ItemDto update(ItemDto itemDto, Long ownerId);

    List<ItemWithBookingDto> get(Long ownerId);

    ItemWithBookingDto getItemById(Long id, Long ownerId);

    List<ItemDto> search(String text);

    CommentDto postComment(CommentDto commentDto, Long authorId, Long itemId);
}
