package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/*
Класс для преобразования экземпляров User и UserDto друг в друга
*/

public class UserMapper {
    // Метод получает экземпляр User и возвращает экземпляр UserDto
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    // Метод получает экземпляр User и соответствующий ему экземпляр UserDto и возвращает обновленный экземпляр User
    // Метод используется для обновления данных экземпляра User в соответствии с полученным из контроллера UserDto
    public static User toUser(UserDto userDto, User user) {
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        return user;
    }
}
