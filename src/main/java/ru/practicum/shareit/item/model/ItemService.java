package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/*
Интерфейс сервисного класса для взаимодействия с вещами
create(Item, int) создает новую вещь
update(ItemDto, int) обновляет существующую вещь
get(int) возвращает список вещей пользователя
getItemById(int) возвращает вещь по id
search(String) возвращает список подходящих по параметру поиска вещей
*/

public interface ItemService {
    ItemDto create(Item item, int ownerId);

    ItemDto update(ItemDto itemDto, int ownerId);

    List<ItemDto> get(int ownerId);

    ItemDto getItemById(int id);

    List<ItemDto> search(String text);
}
