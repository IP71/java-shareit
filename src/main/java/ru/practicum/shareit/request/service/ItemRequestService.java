package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * Интерфейс сервисного класса для взаимодействия с реквестами
 */

public interface ItemRequestService {
    /**
     * Метод создает новый реквест
     *
     * @param itemRequestDto - объект для создания реквеста
     * @param requestorId    - id автора реквеста
     * @return - возвращает созданный ItemRequest (в виде ItemRequestDto)
     */
    ItemRequestDto create(ItemRequestDto itemRequestDto, Long requestorId);

    /**
     * Метод возвращает реквесты пользователя
     *
     * @param requestorId - id пользователя
     * @return - возвращает список реквестов
     */
    List<ItemRequestDto> getUserRequests(Long requestorId);

    /**
     * Метод возвращает реквесты других пользователей
     *
     * @param requestorId - id пользователя
     * @param from        - с какого реквеста начать
     * @param size        - количество получаемых реквестов
     * @return - возвращает список реквестов
     */
    List<ItemRequestDto> getAllUsersRequests(Long requestorId, int from, int size);

    /**
     * Метод возвращает реквест по id
     *
     * @param requestorId - id автора реквеста
     * @param requestId   - id реквеста
     * @return - возвращает реквест
     */
    ItemRequestDto getRequestById(Long requestorId, Long requestId);
}
