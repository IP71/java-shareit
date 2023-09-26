package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.IllegalAccessException;
import ru.practicum.shareit.item.ItemNotFoundException;
import ru.practicum.shareit.user.UserAlreadyExistsException;
import ru.practicum.shareit.user.UserNotFoundException;

@RestControllerAdvice("ru.practicum.shareit")
public class ErrorHandler {
    @ExceptionHandler({UserNotFoundException.class, ItemNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(Throwable e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleIllegalAccessException(IllegalAccessException e) {
        return new ErrorResponse(e.getMessage());
    }
}
