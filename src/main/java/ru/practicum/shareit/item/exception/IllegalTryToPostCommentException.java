package ru.practicum.shareit.item.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IllegalTryToPostCommentException extends RuntimeException {
    public IllegalTryToPostCommentException(long userId, long itemId) {
        super(String.format("Пользователь с id=%d не брал вещь с id=%d в аренду", userId, itemId));
        log.error("Пользователь с id={} не брал вещь с id={} в аренду", userId, itemId);
    }
}
