package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

/*
Класс для преобразования экземпляров Item и ItemDto друг в друга
*/

public class ItemMapper {
    // Метод получает экземпляр Item и возвращает экземпляр ItemDto
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null);
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
}
