package ru.practicum.shareit.request.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemRequestNotFoundException extends RuntimeException {
    public ItemRequestNotFoundException(Long requestId) {
        super(String.format("Реквест с id=%d не найден", requestId));
        log.error("Реквест с id={} не найден", requestId);
    }
}
