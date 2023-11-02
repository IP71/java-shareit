package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    /**
     * Метод создает нового пользователя
     *
     * @param user - объект для создания пользователя
     * @return - возвращает созданный User (в виде UserDto)
     */
    @Override
    @Transactional
    public UserDto create(User user) {
        user = repository.save(user);
        log.info("Пользователь с id={} был создан", user.getId());
        return UserMapper.toUserDto(user);
    }

    /**
     * Метод обновляет существующего пользователя
     *
     * @param userDto - объект для обновления пользователя
     * @return - возвращает обновленный User (в виде UserDto)
     */
    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        User user = repository.findById(userDto.getId()).orElseThrow(() -> new UserNotFoundException(userDto.getId()));
        user = UserMapper.toUser(userDto, user);
        repository.save(user);
        log.info("Пользователь с id={} был обновлен", user.getId());
        return UserMapper.toUserDto(user);
    }

    /**
     * Метод возвращает список всех пользователей
     *
     * @return - возвращает список пользователей
     */
    @Override
    public List<UserDto> get() {
        List<UserDto> foundUsers = repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("Было найдено {} пользователей", foundUsers.size());
        return foundUsers;
    }

    /**
     * Метод возвращает пользователя по id
     *
     * @param id - id пользователя
     * @return - возвращает пользователя
     */
    @Override
    public UserDto getUserById(long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return UserMapper.toUserDto(user.get());
    }

    /**
     * Метод удаляет пользователя по id
     *
     * @param id - id пользователя
     */
    @Override
    @Transactional
    public void deleteUserById(long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        log.info("Пользователь с id={} был удален", id);
        repository.deleteById(id);
    }
}
