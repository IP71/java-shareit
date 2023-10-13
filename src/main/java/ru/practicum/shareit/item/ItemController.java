package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemService;

import javax.validation.Valid;
import java.util.List;

/*
Контроллер для взаимодействия с вещами
Методы create, update и get получают id пользователя из заголовка "X-Sharer-User-Id"
*/

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Метод создает новую вещь при запросе POST /items
    @PostMapping
    public ItemDto create(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.create(item, ownerId);
    }

    // Метод обновляет существующую вещь при запросе PATCH /items/{id}
    @PatchMapping("/{id}")
    public ItemDto update(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId,
                          @PathVariable int id) {
        itemDto.setId(id);
        return itemService.update(itemDto, ownerId);
    }

    // Метод возвращает список всех вещей пользователя при запросе GET /items
    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.get(ownerId);
    }

    // Метод возвращает вещь по id при запросе GET /items/{id}
    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable int id) {
        return itemService.getItemById(id);
    }

    // Метод возвращает список вещей, найденных по параметру запроса text при запросе GET /items/search
    // Параметр запроса text передается в параметре http запроса text
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
