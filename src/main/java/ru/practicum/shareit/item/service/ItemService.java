package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс сервисного класса для взаимодействия с вещами
 */

public interface ItemService {
    /**
     * Метод создает новую вещь
     *
     * @param item    - объект для создания вещи
     * @param ownerId - id владельца вещи
     * @return - возвращает созданный Item (в виде ItemDto)
     */
    ItemDto create(Item item, Long ownerId);

    /**
     * Метод обновляет существующую вещь
     *
     * @param itemDto - объект для обновления вещи
     * @param ownerId - id владельца вещи
     * @return - возвращает обновленный Item (в виде ItemDto)
     */
    ItemDto update(ItemDto itemDto, Long ownerId);

    /**
     * Метод возвращает список вещей пользователя
     *
     * @param ownerId - id пользователя
     * @param from    - с какой вещи начать
     * @param size    - количество получаемых вещей
     * @return - возвращает список вещей
     */
    List<ItemWithBookingDto> get(Long ownerId, int from, int size);

    /**
     * Метод возвращает вещь по id
     *
     * @param id      - id вещи
     * @param ownerId - id пользователя
     * @return - возвращает вещь
     */
    ItemWithBookingDto getItemById(Long id, Long ownerId);

    /**
     * Метод возвращает список подходящих по параметру поиска вещей
     *
     * @param text - поисковый запрос
     * @param from - с какой вещи начать
     * @param size - количество получаемых вещей
     * @return - возвращает список вещей
     */
    List<ItemDto> search(String text, int from, int size);

    /**
     * Метод добавляет комментарий к вещи по её id
     *
     * @param commentDto - объект комментария
     * @param authorId   - id автора
     * @param itemId     - id вещи
     * @return - возвращает созданный объект Comment (в виде CommentDto)
     */
    CommentDto postComment(CommentDto commentDto, Long authorId, Long itemId);
}
