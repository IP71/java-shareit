package ru.practicum.shareit.user.model;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/*
Интерфейс сервисного класса для взаимодействия с пользователями
create(User) создает нового пользователя
update(UserDto) обновляет существующего пользователя
get() возвращает список всех пользователей
getUserById(int) возвращает пользователя по id
deleteUserById(int) удаляет пользователя по id
isExistEmail(String) возвращает true если пользователь с таким email существует
isExistUser(int) возвращает true если пользователь с таким id существует
*/

public interface UserService {
    UserDto create(User user);

    UserDto update(UserDto userDto);

    List<UserDto> get();

    UserDto getUserById(int id);

    void deleteUserById(int id);

    boolean isExistEmail(String email);

    boolean isExistUser(int id);
}
