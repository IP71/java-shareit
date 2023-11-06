package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * Контроллер для взаимодействия с пользователями (User)
 */

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Метод создает нового пользователя при запросе POST /users
     *
     * @param user - полученный в теле запроса объект User
     * @return - возвращает созданный объект User (в виде UserDto)
     */
    @PostMapping
    public UserDto create(@RequestBody User user) {
        return userService.create(user);
    }

    /**
     * Метод обновляет существующего пользователя при запросе PATCH /users/{id}
     *
     * @param userDto - полученный в теле запроса объект UserDto
     * @param id      - id изменяемого пользователя
     * @return - возвращает измененный объект User (в виде UserDto)
     */
    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable long id) {
        userDto.setId(id);
        return userService.update(userDto);
    }

    /**
     * Метод возвращает список всех пользователей при запросе GET /users
     *
     * @return - возвращает список пользователей
     */
    @GetMapping
    public List<UserDto> get() {
        return userService.get();
    }

    /**
     * Метод возвращает пользователя по id при запросе GET /users/{id}
     *
     * @param id - id пользователя
     * @return - возвращает пользователя
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    /**
     * Метод удаляет пользователя по id при запросе DELETE users/{id}
     *
     * @param id - id пользователя
     */
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        userService.deleteUserById(id);
    }
}
