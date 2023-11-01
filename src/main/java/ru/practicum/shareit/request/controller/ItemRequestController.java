package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

/*
Контроллер для взаимодействия с реквестами
Все методы получают id пользователя из заголовка "X-Sharer-User-Id"
*/

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    // Метод создает новый реквест при запросе POST /requests
    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.create(itemRequestDto, requestorId);
    }

    // Метод возвращает реквесты пользователя при запросе GET /requests
    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.getUserRequests(requestorId);
    }

    // Метод возвращает реквесты других пользователей при запросе GET /requests/all
    @GetMapping("/all")
    public List<ItemRequestDto> getAllUsersRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "20") int size) {
        return itemRequestService.getAllUsersRequests(requestorId, from, size);
    }

    // Метод возвращает реквест по id при запросе GET /requests/{requestId}
    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                         @PathVariable Long requestId) {
        return itemRequestService.getRequestById(requestorId, requestId);
    }
}
