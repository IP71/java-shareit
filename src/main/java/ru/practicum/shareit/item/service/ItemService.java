package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/*
Интерфейс сервисного класса для взаимодействия с вещами
create(Item, Long) создает новую вещь
update(ItemDto, Long) обновляет существующую вещь
get(Long) возвращает список вещей пользователя
getItemById(Long) возвращает вещь по id
search(String) возвращает список подходящих по параметру поиска вещей
postComment(CommentDto, Long, Long) добавляет комментарий к вещи по её id
*/

public interface ItemService {
    ItemDto create(Item item, Long ownerId);

    ItemDto update(ItemDto itemDto, Long ownerId);

    List<ItemWithBookingDto> get(Long ownerId);

    ItemWithBookingDto getItemById(Long id, Long ownerId);

    List<ItemDto> search(String text);

    CommentDto postComment(CommentDto commentDto, Long authorId, Long itemId);
}
