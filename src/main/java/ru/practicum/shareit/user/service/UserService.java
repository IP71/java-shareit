package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Интерфейс сервисного класса для взаимодействия с пользователями
 */

public interface UserService {
    /**
     * Метод создает нового пользователя
     *
     * @param user - объект для создания пользователя
     * @return - возвращает созданный User (в виде UserDto)
     */
    UserDto create(User user);

    /**
     * Метод обновляет существующего пользователя
     *
     * @param userDto - объект для обновления пользователя
     * @return - возвращает обновленный User (в виде UserDto)
     */
    UserDto update(UserDto userDto);

    /**
     * Метод возвращает список всех пользователей
     *
     * @return - возвращает список пользователей
     */
    List<UserDto> get();

    /**
     * Метод возвращает пользователя по id
     *
     * @param id - id пользователя
     * @return - возвращает пользователя
     */
    UserDto getUserById(long id);

    /**
     * Метод удаляет пользователя по id
     *
     * @param id - id пользователя
     */
    void deleteUserById(long id);
}
