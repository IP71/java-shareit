package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * Контроллер для взаимодействия с вещами (Item)
 */

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    /**
     * Метод создает новую вещь при запросе POST /items
     *
     * @param item    - полученный в теле запроса объект Item
     * @param ownerId - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @return - возвращает созданный объект Item(в виде ItemDto)
     */
    @PostMapping
    public ItemDto create(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.create(item, ownerId);
    }

    /**
     * Метод обновляет существующую вещь при запросе PATCH /items/{id}
     *
     * @param itemDto - полученный в теле запроса объект ItemDto
     * @param ownerId - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param id      - id изменяемой вещи
     * @return - возвращает измененный объект Item(в виде ItemDto)
     */
    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long ownerId,
                          @PathVariable long id) {
        itemDto.setId(id);
        return itemService.update(itemDto, ownerId);
    }

    /**
     * Метод возвращает список всех вещей пользователя при запросе GET /items
     *
     * @param ownerId - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param from    - с какой вещи начать
     * @param size    - количество получаемых вещей
     * @return - возвращает список вещей
     */
    @GetMapping
    public List<ItemWithBookingDto> get(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "20") int size) {
        return itemService.get(ownerId, from, size);
    }

    /**
     * Метод возвращает вещь по id при запросе GET /items/{id}
     *
     * @param id      - id вещи
     * @param ownerId - id пользователя
     * @return - возвращает вещь
     */
    @GetMapping("/{id}")
    public ItemWithBookingDto getItemById(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getItemById(id, ownerId);
    }

    /**
     * Метод возвращает список вещей, найденных по параметру запроса text при запросе GET /items/search
     *
     * @param text - поисковый запрос (передается в параметре запроса text)
     * @param from - с какой вещи начать
     * @param size - количество получаемых вещей
     * @return - возвращает список вещей
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "20") int size) {
        return itemService.search(text, from, size);
    }

    /**
     * Метод добавляет комментарий к вещи
     *
     * @param commentDto - полученный в теле запроса объект CommentDto
     * @param authorId   - id автора
     * @param itemId     - id вещи, к которой добавляется комментарий
     * @return - возвращает созданный объект Comment (в виде CommentDto)
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestBody CommentDto commentDto,
                                  @RequestHeader("X-Sharer-User-Id") long authorId, @PathVariable long itemId) {
        return itemService.postComment(commentDto, authorId, itemId);
    }
}
