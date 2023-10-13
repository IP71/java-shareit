package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

/*
Класс, работающий с информацией о пользователях и хранящий данные в оперативной памяти
*/

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    // Метод создает нового пользователя
    @Override
    public UserDto create(User user) {
        user.setId(++id);
        users.put(id, user);
        log.info("Пользователь с id={} был создан", user.getId());
        emails.add(user.getEmail());
        return UserMapper.toUserDto(user);
    }

    // Метод обновляет существующего пользователя
    @Override
    public UserDto update(UserDto userDto) {
        if (!users.containsKey(userDto.getId())) {
            log.error("Пользователь с id={} не найден", userDto.getId());
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден", userDto.getId()));
        }
        emails.remove(users.get(userDto.getId()).getEmail());
        users.put(userDto.getId(), UserMapper.toUser(userDto, users.get(userDto.getId())));
        log.info("Пользователь с id={} был обновлен", userDto.getId());
        emails.add(users.get(userDto.getId()).getEmail());
        return UserMapper.toUserDto(users.get(userDto.getId()));
    }

    // Метод возвращает информацию обо всех пользователях
    @Override
    public List<UserDto> get() {
        return users.values().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    // Метод возвращает информацию о пользователе по id
    @Override
    public UserDto getUserById(int id) {
        if (!users.containsKey(id)) {
            log.error("Пользователь с id={} не найден", id);
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден", id));
        }
        return UserMapper.toUserDto(users.get(id));
    }

    // Метод удаляет пользователя по id
    @Override
    public void deleteUserById(int id) {
        if (!users.containsKey(id)) {
            log.error("Пользователь с id={} не найден", id);
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден", id));
        }
        log.info("Пользователь с id={} был удален", id);
        emails.remove(users.get(id).getEmail());
        users.remove(id);
    }

    // Метод возвращает true если пользователь с таким email существует
    @Override
    public boolean isExistEmail(String email) {
        return emails.contains(email);
    }

    // Метод возвращает true если пользователь с таким id существует
    @Override
    public boolean isExistUser(int id) {
        return users.containsKey(id);
    }
}
