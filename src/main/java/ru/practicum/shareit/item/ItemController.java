package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemService;

import javax.validation.Valid;
import java.util.List;

/*
Контроллер для взаимодействия с вещами.
create создает новую вещь при запросе POST /items
update обновляет существующую вещь при запросе PATCH /items/{id}
get возвращает список всех вещей пользователя при запросе GET /items
create, update и get получают id пользователя из заголовка "X-Sharer-User-Id"
getItemById возвращает вещь по id при запросе GET /items/{id}
search возвращает список вещей, найденных по параметру запроса text при запросе GET /items/search
*/

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.create(item, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId,
                          @PathVariable int id) {
        itemDto.setId(id);
        return itemService.update(itemDto, ownerId);
    }

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.get(ownerId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable int id) {
        return itemService.getItemById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
