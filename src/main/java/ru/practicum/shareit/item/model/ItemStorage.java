package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/*
Интерфейс для класса, хранящего данные о вещах
create(Item, int) создает новую вещь
update(ItemDto, int) обновляет существующую вещь
get(int) возвращает список вещей пользователя
getItemById(int) возвращает вещь по id
search(String) возвращает список найденных вещей по поисковому запросу text, полученному из параметра http запроса text
*/

public interface ItemStorage {
    ItemDto create(Item item, int ownerId);

    ItemDto update(ItemDto itemDto, int ownerId);

    List<ItemDto> get(int ownerId);

    ItemDto getItemById(int id);

    List<ItemDto> search(String text);
}
