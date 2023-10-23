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

/*
Сервисный класс, имплементирующий интерфейс UserService
*/

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public UserDto create(User user) {
        user = repository.save(user);
        log.info("Пользователь с id={} был создан", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        if (!repository.existsById(userDto.getId())) {
            throw new UserNotFoundException(userDto.getId());
        }
        User user = repository.findById(userDto.getId()).get();
        String oldEmail = user.getEmail();
        user = UserMapper.toUser(userDto, user);
        repository.save(user);
        log.info("Пользователь с id={} был обновлен", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> get() {
        List<UserDto> foundUsers = repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("Было найдено {} пользователей", foundUsers.size());
        return foundUsers;
    }

    @Override
    public UserDto getUserById(long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return UserMapper.toUserDto(user.get());
    }

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
