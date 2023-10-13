package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserService;

import javax.validation.Valid;
import java.util.List;

/*
Контроллер для взаимодействия с пользователями
*/

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Метод создает нового пользователя при запросе POST /users
    @PostMapping
    public UserDto create(@Valid @RequestBody User user) {
        if (userService.isExistEmail(user.getEmail())) {
            log.error("Пользователь с email={} уже существует", user.getEmail());
            throw new UserAlreadyExistsException(String.format("Пользователь с email=%s уже существует",
                    user.getEmail()));
        }
        return userService.create(user);
    }

    // Метод обновляет существующего пользователя при запросе PATCH /users/{id}
    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable int id) {
        userDto.setId(id);
        if (userService.isExistEmail(userDto.getEmail()) &&
                !userDto.getEmail().equals(getUserById(userDto.getId()).getEmail())) {
            log.error("Пользователь с email={} уже существует", userDto.getEmail());
            throw new UserAlreadyExistsException(String.format("Пользователь с email=%s уже существует",
                    userDto.getEmail()));
        }
        return userService.update(userDto);
    }

    // Метод возвращает список всех пользователей при запросе GET /users
    @GetMapping
    public List<UserDto> get() {
        return userService.get();
    }

    // Метод возвращает пользователя по id при запросе GET /users/{id}
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    // Метод удаляет пользователя по id при запросе DELETE users/{id}
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
    }
}
