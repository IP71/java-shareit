package ru.practicum.shareit.request.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FromIsNegativeException extends RuntimeException {
    public FromIsNegativeException(int from) {
        super(String.format("Было передано отрицательное значение from=%d", from));
        log.error("Было передано отрицательное значение from={}", from);
    }
}
