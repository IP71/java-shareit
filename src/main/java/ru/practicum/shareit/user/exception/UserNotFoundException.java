package ru.practicum.shareit.user.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long id) {
        super(String.format("Пользователь с id=%d не найден", id));
        log.error("Пользователь с id={} не найден", id);
    }
}
