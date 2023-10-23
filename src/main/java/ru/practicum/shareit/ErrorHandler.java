package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.exception.IllegalAccessExceptionItem;
import ru.practicum.shareit.item.exception.IllegalTryToPostCommentException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

@RestControllerAdvice("ru.practicum.shareit")
public class ErrorHandler {
    @ExceptionHandler({UserNotFoundException.class, ItemNotFoundException.class, BookingNotFoundException.class,
            IllegalAccessExceptionBooking.class, IllegalAccessExceptionItem.class, BookerAndOwnerAreTheSameException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(Throwable e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ItemNotAvailableException.class, InvalidDateTimeException.class, InvalidStateException.class,
            ThisStatusAlreadySetException.class, IllegalTryToPostCommentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(Throwable e) {
        return new ErrorResponse(e.getMessage());
    }
}
