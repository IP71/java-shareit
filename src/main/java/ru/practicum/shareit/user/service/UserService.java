package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/*
Интерфейс сервисного класса для взаимодействия с пользователями
create(User) создает нового пользователя
update(UserDto) обновляет существующего пользователя
get() возвращает список всех пользователей
getUserById(long) возвращает пользователя по id
deleteUserById(long) удаляет пользователя по id
*/

public interface UserService {
    UserDto create(User user);

    UserDto update(UserDto userDto);

    List<UserDto> get();

    UserDto getUserById(long id);

    void deleteUserById(long id);
}
