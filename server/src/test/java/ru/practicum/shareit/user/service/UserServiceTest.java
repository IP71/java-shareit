package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository mockUserRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User user = new User(1L, "user", "user@email.com");

    @Test
    public void create_shouldCreateUser() {
        when(mockUserRepository.save(user)).thenReturn(user);
        assertEquals(UserMapper.toUserDto(user), userService.create(user));
    }

    @Test
    public void update_shouldUpdateUser() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));
        User updatedUser = new User(1L, "new", "new@email.com");
        when(mockUserRepository.save(updatedUser)).thenReturn(updatedUser);
        assertEquals(UserMapper.toUserDto(updatedUser), userService.update(UserMapper.toUserDto(updatedUser)));
    }

    @Test
    public void update_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.empty());
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> userService.update(UserMapper.toUserDto(user)));
        assertEquals("Пользователь с id=1 не найден", e.getMessage());
    }

    @Test
    public void get_shouldReturnUsers() {
        when(mockUserRepository.findAll()).thenReturn(List.of(user));
        assertEquals(List.of(UserMapper.toUserDto(user)), userService.get());
    }

    @Test
    public void getUserById_shouldReturnUser() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(UserMapper.toUserDto(user), userService.getUserById(1L));
    }

    @Test
    public void getUserById_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.empty());
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(1L));
        assertEquals("Пользователь с id=1 не найден", e.getMessage());
    }

    @Test
    public void deleteUserById_shouldDeleteUser() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteUserById(1L);
        verify(mockUserRepository).deleteById(1L);
    }

    @Test
    public void deleteUserById_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.empty());
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> userService.deleteUserById(1L));
        assertEquals("Пользователь с id=1 не найден", e.getMessage());
    }
}
