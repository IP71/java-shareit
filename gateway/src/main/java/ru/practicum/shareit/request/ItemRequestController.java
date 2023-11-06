package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Контроллер для взаимодействия с реквестами (ItemRequest)
 */

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    /**
     * Метод создает новый реквест при запросе POST /requests
     *
     * @param itemRequestDto - полученный в теле запроса объект ItemRequestDto
     * @param userId         - id пользователя, создающего реквест
     * @return - возвращает созданный объект ItemRequest (в виде ItemRequestDto)
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Creating request {}, user={}", itemRequestDto, userId);
        return itemRequestClient.create(userId, itemRequestDto);
    }

    /**
     * Метод возвращает реквесты пользователя при запросе GET /requests
     *
     * @param userId - id пользователя
     * @return - возвращает список реквестов
     */
    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get requests with owner {}", userId);
        return itemRequestClient.getUserRequests(userId);
    }

    /**
     * Метод возвращает реквесты других пользователей при запросе GET /requests/all
     *
     * @param userId - id пользователя
     * @param from   - с какого реквеста начать
     * @param size   - количество получаемых реквестов
     * @return - возвращает список реквестов
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsersRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all requests with userId {}, from {}, size {}", userId, from, size);
        return itemRequestClient.getAllUsersRequests(userId, from, size);
    }

    /**
     * Метод возвращает реквест по id при запросе GET /requests/{requestId}
     *
     * @param userId    - id пользователя
     * @param requestId - id реквеста
     * @return - возвращает реквест
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable long requestId) {
        log.info("Get request {}, userId {}", requestId, userId);
        return itemRequestClient.getRequestById(userId, requestId);
    }
}
