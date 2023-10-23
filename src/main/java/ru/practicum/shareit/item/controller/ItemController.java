package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/*
Контроллер для взаимодействия с вещами
Методы create, update и get получают id пользователя из заголовка "X-Sharer-User-Id"
*/

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    // Метод создает новую вещь при запросе POST /items
    @PostMapping
    public ItemDto create(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.create(item, ownerId);
    }

    // Метод обновляет существующую вещь при запросе PATCH /items/{id}
    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long ownerId,
                          @PathVariable long id) {
        itemDto.setId(id);
        return itemService.update(itemDto, ownerId);
    }

    // Метод возвращает список всех вещей пользователя при запросе GET /items
    @GetMapping
    public List<ItemWithBookingDto> get(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.get(ownerId);
    }

    // Метод возвращает вещь по id при запросе GET /items/{id}
    @GetMapping("/{id}")
    public ItemWithBookingDto getItemById(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getItemById(id, ownerId);
    }

    // Метод возвращает список вещей, найденных по параметру запроса text при запросе GET /items/search
    // Параметр запроса text передается в параметре http запроса text
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    // Метод добавляет комментарий к вещи
    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@Valid @RequestBody CommentDto commentDto,
                                  @RequestHeader("X-Sharer-User-Id") long authorId, @PathVariable long itemId) {
        return itemService.postComment(commentDto, authorId, itemId);
    }
}
