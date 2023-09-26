package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserService;
import ru.practicum.shareit.user.model.UserStorage;

import java.util.List;

/*
Сервисный класс, имплементирующий интерфейс UserService
*/

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto create(User user) {
        return userStorage.create(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        return userStorage.update(userDto);
    }

    @Override
    public List<UserDto> get() {
        return userStorage.get();
    }

    @Override
    public UserDto getUserById(int id) {
        return userStorage.getUserById(id);
    }

    @Override
    public void deleteUserById(int id) {
        userStorage.deleteUserById(id);
    }

    @Override
    public boolean isExistEmail(String email) {
        return userStorage.isExistEmail(email);
    }

    @Override
    public boolean isExistUser(int id) {
        return userStorage.isExistUser(id);
    }
}
