package ru.practicum.shareit.item.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IllegalAccessExceptionItem extends RuntimeException {
    public IllegalAccessExceptionItem(long ownerId, long itemId) {
        super(String.format("Пользователь с id=%d не владелец вещи с id=%d", ownerId, itemId));
        log.error("Пользователь с id={} не владелец вещи с id={}", ownerId, itemId);
    }
}
