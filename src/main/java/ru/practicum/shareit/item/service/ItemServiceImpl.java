package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.exception.IllegalAccessExceptionItem;
import ru.practicum.shareit.item.exception.IllegalTryToPostCommentException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // Метод создает новую вещь
    @Override
    @Transactional
    public ItemDto create(Item item, Long ownerId) {
        checkIfOwnerExists(ownerId);
        item.setOwner(ownerId);
        item = itemRepository.save(item);
        log.info("Вещь с id={} была создана", item.getId());
        return ItemMapper.toItemDto(item);
    }

    //Метод обновляет существующую вещь
    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Long ownerId) {
        if (!itemRepository.existsById(itemDto.getId())) {
            throw new ItemNotFoundException(itemDto.getId());
        }
        checkIfOwnerExists(ownerId);
        Item item = itemRepository.findById(itemDto.getId()).get();
        if (!item.getOwner().equals(ownerId)) {
            throw new IllegalAccessExceptionItem(ownerId, item.getId());
        }
        item = ItemMapper.toItem(itemDto, item);
        itemRepository.save(item);
        log.info("Вещь с id={} была обновлена", itemDto.getId());
        return ItemMapper.toItemDto(item);
    }

    // Метод возвращает список вещей пользователя
    @Override
    public List<ItemWithBookingDto> get(Long ownerId) {
        checkIfOwnerExists(ownerId);
        List<Item> foundItems = itemRepository.findAllByOwner(ownerId);
        log.info("Было найдено {} вещей, принадлежащих пользователю с id={}", foundItems.size(), ownerId);
        List<ItemWithBookingDto> foundItemsWithBookingDto = new ArrayList<>();
        LocalDateTime date = LocalDateTime.now();
        for (Item item : foundItems) {
            foundItemsWithBookingDto.add(ItemMapper.toItemWithBookingDto(item,
                    BookingMapper.toBookingDto(bookingRepository.findFirst1ByItemIdAndStartIsBeforeAndStatusIsNotOrderByEndDesc(item.getId(), date, Status.REJECTED).orElse(null)),
                    BookingMapper.toBookingDto(bookingRepository.findFirst1ByItemIdAndStartIsAfterAndStatusIsNotOrderByStartAsc(item.getId(), date, Status.REJECTED).orElse(null)),
                    commentRepository.findAllByItemId(item.getId()).stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList())));
        }
        return foundItemsWithBookingDto;
    }

    // Метод возвращает вещь по id
    @Override
    public ItemWithBookingDto getItemById(Long id, Long ownerId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        if (!item.getOwner().equals(ownerId)) {
            return ItemMapper.toItemWithBookingDto(item, null, null,
                    commentRepository.findAllByItemId(item.getId()).stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList()));
        } else {
            LocalDateTime date = LocalDateTime.now();
            return ItemMapper.toItemWithBookingDto(item,
                    BookingMapper.toBookingDto(bookingRepository.findFirst1ByItemIdAndStartIsBeforeAndStatusIsNotOrderByEndDesc(item.getId(), date, Status.REJECTED).orElse(null)),
                    BookingMapper.toBookingDto(bookingRepository.findFirst1ByItemIdAndStartIsAfterAndStatusIsNotOrderByStartAsc(item.getId(), date, Status.REJECTED).orElse(null)),
                    commentRepository.findAllByItemId(item.getId()).stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList()));
        }
    }

    // Метод возвращает список подходящих по параметру поиска вещей
    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<ItemDto> foundItems = itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("По запросу '{}' было найдено {} вещей", text, foundItems.size());
        return foundItems;
    }

    // Метод добавляет комментарий к вещи по её id
    @Override
    public CommentDto postComment(CommentDto commentDto, Long authorId, Long itemId) {
        User user = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException(authorId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByEndDesc(authorId, LocalDateTime.now());
        bookings.stream()
                .filter(booking -> booking.getBooker().getId().equals(authorId))
                .findAny().orElseThrow(() -> new IllegalTryToPostCommentException(authorId, itemId));
        Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, item, user));
        log.info("Комментарий с id={} был создан", comment.getId());
        return CommentMapper.toCommentDto(comment);
    }

    // Метод проверяет, существует ли пользователь с таким id
    private void checkIfOwnerExists(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new UserNotFoundException(ownerId);
        }
    }
}
