package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemStorage;

import java.util.*;
import java.util.stream.Collectors;

/*
Класс, имплементирующий интерфейс ItemStorage, и хранящий данные в оперативной памяти
*/

@Slf4j
@Component
public class InMemoryItemStorage implements ItemStorage {
    private int id = 0;
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public ItemDto create(Item item, int ownerId) {
        item.setId(++id);
        item.setOwner(ownerId);
        items.put(item.getId(), item);
        log.info("Вещь с id={} была создана", item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto itemDto, int ownerId) {
        if (!items.containsKey(itemDto.getId())) {
            log.error("Вещь с id={} не найдена", itemDto.getId());
            throw new ItemNotFoundException(String.format("Вещь с id=%d не найдена", itemDto.getId()));
        }
        if (items.get(itemDto.getId()).getOwner() != ownerId) {
            log.error("Ошибка - редактировать вещь может только владелец");
            throw new IllegalAccessException("Ошибка - редактировать вещь может только владелец");
        }
        items.put(itemDto.getId(), ItemMapper.toItem(itemDto, items.get(itemDto.getId())));
        log.info("Вешь с id={} была обновлена", itemDto.getId());
        return ItemMapper.toItemDto(items.get(itemDto.getId()));
    }

    @Override
    public List<ItemDto> get(int ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == ownerId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(int id) {
        if (!items.containsKey(id)) {
            log.error("Вещь с id={} не найдена", id);
            throw new ItemNotFoundException(String.format("Вещь с id=%d не найдена", id));
        }
        return ItemMapper.toItemDto(items.get(id));
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> foundItems = new ArrayList<>();
        if (text.isBlank()) {
            return Collections.EMPTY_LIST;
        }
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable()) {
                foundItems.add(item);
            }
        }
        log.info("По запросу '{}' найдено {} вещей", text, foundItems.size());
        return foundItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
