package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для взаимодействия с реквестами (ItemRequest)
 */

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    /**
     * Метод создает новый реквест при запросе POST /requests
     *
     * @param itemRequestDto - полученный в теле запроса объект ItemRequestDto
     * @param requestorId    - id пользователя, создающего реквест
     * @return - возвращает созданный объект ItemRequest (в виде ItemRequestDto)
     */
    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.create(itemRequestDto, requestorId);
    }

    /**
     * Метод возвращает реквесты пользователя при запросе GET /requests
     *
     * @param requestorId - id пользователя
     * @return - возвращает список реквестов
     */
    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.getUserRequests(requestorId);
    }

    /**
     * Метод возвращает реквесты других пользователей при запросе GET /requests/all
     *
     * @param requestorId - id пользователя
     * @param from        - с какого реквеста начать
     * @param size        - количество получаемых реквестов
     * @return - возвращает список реквестов
     */
    @GetMapping("/all")
    public List<ItemRequestDto> getAllUsersRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "20") int size) {
        return itemRequestService.getAllUsersRequests(requestorId, from, size);
    }

    /**
     * Метод возвращает реквест по id при запросе GET /requests/{requestId}
     *
     * @param requestorId - id пользователя
     * @param requestId   - id реквеста
     * @return - возвращает реквест
     */
    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                         @PathVariable Long requestId) {
        return itemRequestService.getRequestById(requestorId, requestId);
    }
}
