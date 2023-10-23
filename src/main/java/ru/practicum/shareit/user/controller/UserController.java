package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/*
Контроллер для взаимодействия с пользователями
*/

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Метод создает нового пользователя при запросе POST /users
    @PostMapping
    public UserDto create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    // Метод обновляет существующего пользователя при запросе PATCH /users/{id}
    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable long id) {
        userDto.setId(id);
        return userService.update(userDto);
    }

    // Метод возвращает список всех пользователей при запросе GET /users
    @GetMapping
    public List<UserDto> get() {
        return userService.get();
    }

    // Метод возвращает пользователя по id при запросе GET /users/{id}
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    // Метод удаляет пользователя по id при запросе DELETE users/{id}
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        userService.deleteUserById(id);
    }
}
