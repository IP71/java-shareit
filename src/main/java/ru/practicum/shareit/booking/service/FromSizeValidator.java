package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.request.exception.FromIsNegativeException;
import ru.practicum.shareit.request.exception.SizeIsNotPositiveException;

public class FromSizeValidator {
    /**
     * Метод проверяет корректность полученных параметров from и size
     *
     * @param from - номер бронирования, с которого нужно начать получаемый список (>=0)
     * @param size - количество получаемых бронирований (>0)
     */
    public static void checkFromSize(int from, int size) {
        if (from < 0) {
            throw new FromIsNegativeException(from);
        }
        if (size <= 0) {
            throw new SizeIsNotPositiveException(size);
        }
    }
}
