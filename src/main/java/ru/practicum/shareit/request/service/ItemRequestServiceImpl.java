package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.service.FromSizeValidator;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /**
     * Метод создает новый реквест
     *
     * @param itemRequestDto - объект для создания реквеста
     * @param requestorId    - id автора реквеста
     * @return - возвращает созданный ItemRequest (в виде ItemRequestDto)
     */
    @Override
    @Transactional
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Long requestorId) {
        User user = userRepository.findById(requestorId).orElseThrow(() -> new UserNotFoundException(requestorId));
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto, user));
        log.info("Реквест с id={} был создан", itemRequest.getId());
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    /**
     * Метод возвращает реквесты пользователя
     *
     * @param requestorId - id пользователя
     * @return - возвращает список реквестов
     */
    @Override
    public List<ItemRequestDto> getUserRequests(Long requestorId) {
        if (!userRepository.existsById(requestorId)) {
            throw new UserNotFoundException(requestorId);
        }
        List<ItemRequestDto> foundRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        List<Item> foundItems = itemRepository.findAllByRequestIdIn(foundRequests.stream()
                .map(ItemRequestDto::getId)
                .collect(Collectors.toList()));
        for (ItemRequestDto request : foundRequests) {
            List<ItemDto> items = new ArrayList<>();
            for (Item item : foundItems) {
                if (item.getRequestId().equals(request.getId())) {
                    items.add(ItemMapper.toItemDto(item));
                }
            }
            request.setItems(items);
        }
        return foundRequests;
    }

    /**
     * Метод возвращает реквесты других пользователей
     *
     * @param requestorId - id пользователя
     * @param from        - с какого реквеста начать
     * @param size        - количество получаемых реквестов
     * @return - возвращает список реквестов
     */
    @Override
    public List<ItemRequestDto> getAllUsersRequests(Long requestorId, int from, int size) {
        if (!userRepository.existsById(requestorId)) {
            throw new UserNotFoundException(requestorId);
        }
        FromSizeValidator.checkFromSize(from, size);
        List<ItemRequestDto> foundRequests = itemRequestRepository
                .findAllByRequestorIdIsNotOrderByCreatedDesc(requestorId, PageRequest.of(from / size, size))
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        List<Item> foundItems = itemRepository.findAllByRequestIdIn(foundRequests.stream()
                .map(ItemRequestDto::getId)
                .collect(Collectors.toList()));
        for (ItemRequestDto request : foundRequests) {
            List<ItemDto> items = new ArrayList<>();
            for (Item item : foundItems) {
                if (item.getRequestId().equals(request.getId())) {
                    items.add(ItemMapper.toItemDto(item));
                }
            }
            request.setItems(items);
        }
        return foundRequests;
    }

    /**
     * Метод возвращает реквест по id
     *
     * @param requestorId - id автора реквеста
     * @param requestId   - id реквеста
     * @return - возвращает реквест
     */
    @Override
    public ItemRequestDto getRequestById(Long requestorId, Long requestId) {
        if (!userRepository.existsById(requestorId)) {
            throw new UserNotFoundException(requestorId);
        }
        ItemRequestDto request = ItemRequestMapper.toItemRequestDto(itemRequestRepository
                .findById(requestId).orElseThrow(() -> new ItemRequestNotFoundException(requestId)));
        List<ItemDto> foundItems = itemRepository.findAllByRequestIdIn(List.of(requestId)).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        request.setItems(foundItems);
        return request;
    }
}
