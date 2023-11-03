package ru.practicum.shareit.request.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SizeIsNotPositiveException extends RuntimeException {
    public SizeIsNotPositiveException(int size) {
        super(String.format("Было передано не положительное значение size=%d", size));
        log.error("Было передано не положительное значение size={}", size);
    }
}
