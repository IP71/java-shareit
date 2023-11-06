package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Контроллер для взаимодействия с вещами (Item)
 */

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    /**
     * Метод создает новую вещь при запросе POST /items
     *
     * @param itemDto - полученный в теле запроса объект ItemDto
     * @param userId  - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @return - возвращает созданный объект Item(в виде ItemDto)
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.create(userId, itemDto);
    }

    /**
     * Метод обновляет существующую вещь при запросе PATCH /items/{id}
     *
     * @param itemDto - полученный в теле запроса объект ItemDto
     * @param userId  - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param itemId  - id изменяемой вещи
     * @return - возвращает измененный объект Item(в виде ItemDto)
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody ItemDto itemDto,
                                         @PathVariable long itemId) {
        log.info("Updating item {}, userId={}", itemDto, userId);
        return itemClient.update(userId, itemDto, itemId);
    }

    /**
     * Метод возвращает список всех вещей пользователя при запросе GET /items
     *
     * @param userId - полученный из заголовка "X-Sharer-User-Id" id пользователя
     * @param from   - с какой вещи начать
     * @param size   - количество получаемых вещей
     * @return - возвращает список вещей
     */
    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get items with owner {}, from {}, size{}", userId, from, size);
        return itemClient.get(userId, from, size);
    }

    /**
     * Метод возвращает вещь по id при запросе GET /items/{id}
     *
     * @param itemId - id вещи
     * @param userId - id пользователя
     * @return - возвращает вещь
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable long itemId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.getItemById(itemId, userId);
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
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam(name = "text") String text,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get items with text={}, userId={}, from={}, size={}", text, userId, from, size);
        return itemClient.search(userId, text, from, size);
    }

    /**
     * Метод добавляет комментарий к вещи
     *
     * @param commentDto - полученный в теле запроса объект CommentDto
     * @param userId     - id автора
     * @param itemId     - id вещи, к которой добавляется комментарий
     * @return - возвращает созданный объект Comment (в виде CommentDto)
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody @Valid CommentDto commentDto,
                                              @PathVariable long itemId) {
        log.info("Post comment {}, userId={}, itemId={}", commentDto, userId, itemId);
        return itemClient.postComment(userId, commentDto, itemId);
    }
}
