package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.exception.IllegalAccessExceptionItem;
import ru.practicum.shareit.item.exception.IllegalTryToPostCommentException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private CommentRepository mockCommentRepository;
    @Mock
    private BookingRepository mockBookingRepository;
    @Mock
    private ItemRepository mockItemRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ItemRequestRepository mockItemRequestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    private Item item = new Item(1L, "Item", "item", true, 1L, null);
    private User user = new User(1L, "owner", "owner@email.com");
    private Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
            item, user, Status.WAITING);
    private Comment comment = new Comment(1L, "comment", item, user, LocalDateTime.now());

    @BeforeEach
    public void setFields() {
        item = new Item(1L, "Item", "item", true, 1L, null);
    }

    @Test
    public void create_shouldCreateItem() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockItemRepository.save(item)).thenReturn(item);
        assertEquals(toItemDto(item), itemService.create(item, 1L));
    }

    @Test
    public void create_shouldThrowWhenOwnerNotFound() {
        when(mockUserRepository.existsById(1L)).thenReturn(false);
        UserNotFoundException e = assertThrows(UserNotFoundException.class, () -> itemService.create(item, 1L));
        assertEquals("Пользователь с id=1 не найден", e.getMessage());
    }

    @Test
    public void create_shouldThrowWhenRequestNotFound() {
        item.setRequestId(1L);
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockItemRequestRepository.findById(1L)).thenReturn(Optional.empty());
        ItemRequestNotFoundException e = assertThrows(ItemRequestNotFoundException.class,
                () -> itemService.create(item, 1L));
        assertEquals("Реквест с id=1 не найден", e.getMessage());
    }

    @Test
    public void update_shouldUpdateItem() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        Item updatedItem = new Item(1L, "ItemUpdated", "item", true, 1L, null);
        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(mockItemRepository.save(any())).thenReturn(updatedItem);
        assertEquals(toItemDto(updatedItem), itemService.update(toItemDto(item), 1L));
    }

    @Test
    public void update_shouldThrowWhenOwnerNotFound() {
        when(mockUserRepository.existsById(1L)).thenReturn(false);
        UserNotFoundException e = assertThrows(UserNotFoundException.class, () -> itemService.update(toItemDto(item), 1L));
        assertEquals("Пользователь с id=1 не найден", e.getMessage());
    }

    @Test
    public void update_shouldThrowWhenItemNotFound() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockItemRepository.findById(1L)).thenReturn(Optional.empty());
        ItemNotFoundException e = assertThrows(ItemNotFoundException.class,
                () -> itemService.update(toItemDto(item), 1L));
        assertEquals("Вещь с id=1 не найдена", e.getMessage());
    }

    @Test
    public void update_shouldThrowWhenUserIsNotOwner() {
        item.setOwner(2L);
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item));
        IllegalAccessExceptionItem e = assertThrows(IllegalAccessExceptionItem.class,
                () -> itemService.update(toItemDto(item), 1L));
        assertEquals("Пользователь с id=1 не владелец вещи с id=1", e.getMessage());
    }

    @Test
    public void get_shouldReturnItems() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);
        when(mockItemRepository.findAllByOwner(1L, PageRequest.of(1, 1))).thenReturn(List.of(item));
        when(mockBookingRepository.findFirst1ByItemIdAndStartIsAfterAndStatusIsNotOrderByStartAsc(any(), any(), any())).thenReturn(Optional.of(booking));
        when(mockBookingRepository.findFirst1ByItemIdAndStartIsBeforeAndStatusIsNotOrderByEndDesc(any(), any(), any())).thenReturn(Optional.of(booking));
        when(mockCommentRepository.findAllByItemId(1L)).thenReturn(List.of(comment));
        List<ItemWithBookingDto> result = List.of(new ItemWithBookingDto(1L, item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequestId(), BookingMapper.toBookingDto(booking),
                BookingMapper.toBookingDto(booking), List.of(CommentMapper.toCommentDto(comment))));
        assertEquals(result, itemService.get(1L, 1, 1));
    }

    @Test
    public void getItemById_shouldReturnItemWhenUserIsOwner() {
        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(mockBookingRepository.findFirst1ByItemIdAndStartIsAfterAndStatusIsNotOrderByStartAsc(any(), any(), any())).thenReturn(Optional.of(booking));
        when(mockBookingRepository.findFirst1ByItemIdAndStartIsBeforeAndStatusIsNotOrderByEndDesc(any(), any(), any())).thenReturn(Optional.of(booking));
        when(mockCommentRepository.findAllByItemId(1L)).thenReturn(List.of(comment));
        ItemWithBookingDto result = new ItemWithBookingDto(1L, item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequestId(), BookingMapper.toBookingDto(booking),
                BookingMapper.toBookingDto(booking), List.of(CommentMapper.toCommentDto(comment)));
        assertEquals(result, itemService.getItemById(1L, 1L));
    }

    @Test
    public void getItemById_shouldReturnItemWhenUserIsNotOwner() {
        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(mockCommentRepository.findAllByItemId(1L)).thenReturn(List.of(comment));
        ItemWithBookingDto result = new ItemWithBookingDto(1L, item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequestId(), null, null, List.of(CommentMapper.toCommentDto(comment)));
        assertEquals(result, itemService.getItemById(1L, 2L));
    }

    @Test
    public void getItemById_shouldThrowWhenItemNotFound() {
        when(mockItemRepository.findById(1L)).thenReturn(Optional.empty());
        ItemNotFoundException e = assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(1L, 1L));
        assertEquals("Вещь с id=1 не найдена", e.getMessage());
    }

    @Test
    public void search_shouldReturnItems() {
        when(mockItemRepository.search(anyString(), any())).thenReturn(List.of(item));
        assertEquals(List.of(toItemDto(item)), itemService.search("text", 1, 1));
    }

    @Test
    public void search_shouldReturnEmptyListWhenTextIsBlank() {
        assertEquals(Collections.EMPTY_LIST, itemService.search("  ", 1, 1));
    }

    @Test
    public void postComment_shouldPostComment() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(mockBookingRepository.findAllByBookerIdAndEndIsBeforeOrderByEndDesc(any(), any())).thenReturn(List.of(booking));
        when(mockCommentRepository.save(comment)).thenReturn(comment);
        assertEquals(CommentMapper.toCommentDto(comment), itemService.postComment(CommentMapper.toCommentDto(comment), 1L, 1L));
    }

    @Test
    public void postComment_shouldThrowWhenUserNotFound() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.empty());
        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> itemService.postComment(CommentMapper.toCommentDto(comment), 1L, 1L));
        assertEquals("Пользователь с id=1 не найден", e.getMessage());
    }

    @Test
    public void postComment_shouldThrowWhenItemNotFound() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mockItemRepository.findById(1L)).thenReturn(Optional.empty());
        ItemNotFoundException e = assertThrows(ItemNotFoundException.class,
                () -> itemService.postComment(CommentMapper.toCommentDto(comment), 1L, 1L));
        assertEquals("Вещь с id=1 не найдена", e.getMessage());
    }

    @Test
    public void postComment_shouldThrowWhenUserIsNotBooker() {
        when(mockUserRepository.findById(2L)).thenReturn(Optional.of(user));
        when(mockItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(mockBookingRepository.findAllByBookerIdAndEndIsBeforeOrderByEndDesc(any(), any())).thenReturn(List.of(booking));
        IllegalTryToPostCommentException e = assertThrows(IllegalTryToPostCommentException.class,
                () -> itemService.postComment(CommentMapper.toCommentDto(comment), 2L, 1L));
        assertEquals("Пользователь с id=2 не брал вещь с id=1 в аренду", e.getMessage());
    }
}
