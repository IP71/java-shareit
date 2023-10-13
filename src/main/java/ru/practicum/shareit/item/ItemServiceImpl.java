package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.item.model.ItemStorage;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.model.UserService;

import java.util.List;

/*
Сервисный класс, имплементирующий интерфейс ItemService
Метод checkIfOwnerExists выбрасывает исключение, если был передан id несуществующего пользователя
*/

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    public ItemServiceImpl(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    @Override
    public ItemDto create(Item item, int ownerId) {
        checkIfOwnerExists(ownerId);
        return itemStorage.create(item, ownerId);
    }

    @Override
    public ItemDto update(ItemDto itemDto, int ownerId) {
        checkIfOwnerExists(ownerId);
        return itemStorage.update(itemDto, ownerId);
    }

    @Override
    public List<ItemDto> get(int ownerId) {
        checkIfOwnerExists(ownerId);
        return itemStorage.get(ownerId);
    }

    @Override
    public ItemDto getItemById(int id) {
        return itemStorage.getItemById(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemStorage.search(text);
    }

    private void checkIfOwnerExists(int ownerId) {
        if (!userService.isExistUser(ownerId)) {
            log.error("Пользователь с id={} не найден", ownerId);
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден", ownerId));
        }
    }
}
