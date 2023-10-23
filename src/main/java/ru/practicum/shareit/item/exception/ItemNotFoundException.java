package ru.practicum.shareit.item.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(long id) {
        super(String.format("Вещь с id=%d не найдена", id));
        log.error("Вещь с id={} не найдена", id);
    }
}
