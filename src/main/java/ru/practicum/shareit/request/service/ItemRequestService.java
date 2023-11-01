package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/*
Интерфейс сервисного класса для взаимодействия с реквестами
create(ItemRequestDto, Long) создает новый реквест
getUserRequests(Long) возвращает реквесты пользователя
getAllUsersRequests(Long, int, int) возвращает реквесты других пользователей
getRequestById(Long, Long) возвращает реквест по id
*/

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto, Long requestorId);

    List<ItemRequestDto> getUserRequests(Long requestorId);

    List<ItemRequestDto> getAllUsersRequests(Long requestorId, int from, int size);

    ItemRequestDto getRequestById(Long requestorId, Long requestId);
}
