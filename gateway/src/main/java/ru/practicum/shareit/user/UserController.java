package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

/**
 * Контроллер для взаимодействия с пользователями (User)
 */

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    /**
     * Метод создает нового пользователя при запросе POST /users
     *
     * @param userDto - полученный в теле запроса объект UserDto
     * @return - возвращает созданный объект User (в виде UserDto)
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto userDto) {
        log.info("Creating user {}", userDto);
        return userClient.create(userDto);
    }

    /**
     * Метод обновляет существующего пользователя при запросе PATCH /users/{id}
     *
     * @param userDto - полученный в теле запроса объект UserDto
     * @param userId  - id изменяемого пользователя
     * @return - возвращает измененный объект User (в виде UserDto)
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody UserDto userDto,
                                         @PathVariable long userId) {
        log.info("Updating user {}, userId={}", userDto, userId);
        return userClient.update(userId, userDto);
    }

    /**
     * Метод возвращает список всех пользователей при запросе GET /users
     *
     * @return - возвращает список пользователей
     */
    @GetMapping
    public ResponseEntity<Object> get() {
        log.info("Get all users");
        return userClient.get();
    }

    /**
     * Метод возвращает пользователя по id при запросе GET /users/{id}
     *
     * @param userId - id пользователя
     * @return - возвращает пользователя
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        log.info("Get user {}", userId);
        return userClient.getUserById(userId);
    }

    /**
     * Метод удаляет пользователя по id при запросе DELETE users/{id}
     *
     * @param userId - id пользователя
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable long userId) {
        log.info("Deleting user {}", userId);
        return userClient.deleteUserById(userId);
    }
}
