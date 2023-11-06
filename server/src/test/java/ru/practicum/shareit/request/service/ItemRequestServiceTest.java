package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.request.dto.ItemRequestMapper.toItemRequestDto;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository mockItemRequestRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ItemRepository mockItemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    private User user = new User(1L, "owner", "owner@email.com");
    private Item item = new Item(1L, "Item", "item", true, 1L, 1L);
    private ItemRequest itemRequest = new ItemRequest(1L, "request", user, LocalDateTime.now());

    @Test
    public void create_shouldCreateRequest() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mockItemRequestRepository.save(itemRequest)).thenReturn(itemRequest);
        assertEquals(toItemRequestDto(itemRequest), itemRequestService.create(toItemRequestDto(itemRequest), 1L));
    }

    @Test
    public void create_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.empty());
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> itemRequestService.create(toItemRequestDto(itemRequest), 1L));
        assertEquals("Пользователь с id=1 не найден", e.getMessage());
    }

    @Test
    public void getUserRequests_shouldReturnRequests() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockItemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(1L)).thenReturn(List.of(itemRequest));
        when(mockItemRepository.findAllByRequestIdIn(any())).thenReturn(List.of(item));
        ItemRequestDto result = toItemRequestDto(itemRequest);
        result.setItems(List.of(ItemMapper.toItemDto(item)));
        assertEquals(List.of(result), itemRequestService.getUserRequests(1L));
    }

    @Test
    public void getUserRequests_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.existsById(1L)).thenReturn(false);
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> itemRequestService.getUserRequests(1L));
        assertEquals("Пользователь с id=1 не найден", e.getMessage());
    }

    @Test
    public void getAllUsersRequests_shouldReturnRequests() {
        when(mockUserRepository.existsById(2L)).thenReturn(true);
        when(mockItemRequestRepository.findAllByRequestorIdIsNotOrderByCreatedDesc(any(), any())).thenReturn(List.of(itemRequest));
        when(mockItemRepository.findAllByRequestIdIn(any())).thenReturn(List.of(item));
        ItemRequestDto result = toItemRequestDto(itemRequest);
        result.setItems(List.of(ItemMapper.toItemDto(item)));
        assertEquals(List.of(result), itemRequestService.getAllUsersRequests(2L, 1, 1));
    }

    @Test
    public void getAllUsersRequests_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.existsById(1L)).thenReturn(false);
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> itemRequestService.getAllUsersRequests(1L, 1, 1));
        assertEquals("Пользователь с id=1 не найден", e.getMessage());
    }

    @Test
    public void getRequestById_shouldReturnRequest() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockItemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
        when(mockItemRepository.findAllByRequestIdIn(any())).thenReturn(List.of(item));
        ItemRequestDto result = toItemRequestDto(itemRequest);
        result.setItems(List.of(ItemMapper.toItemDto(item)));
        assertEquals(result, itemRequestService.getRequestById(1L, 1L));
    }

    @Test
    public void getRequestById_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.existsById(1L)).thenReturn(false);
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> itemRequestService.getRequestById(1L, 1L));
        assertEquals("Пользователь с id=1 не найден", e.getMessage());
    }

    @Test
    public void getRequestById_shouldThrowWhenRequestNotFound() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockItemRequestRepository.findById(1L)).thenReturn(Optional.empty());
        ItemRequestNotFoundException e = assertThrows(ItemRequestNotFoundException.class,
                () -> itemRequestService.getRequestById(1L, 1L));
        assertEquals("Реквест с id=1 не найден", e.getMessage());
    }
}
