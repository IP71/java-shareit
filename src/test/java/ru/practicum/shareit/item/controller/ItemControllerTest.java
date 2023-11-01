package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemService itemService;

    @Test
    public void createTest() throws Exception {
        Item item = new Item(null, "item", "desc", true, 1L, null);
        when(itemService.create(item, 1L)).thenReturn(ItemMapper.toItemDto(item));
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(ItemMapper.toItemDto(item))));
    }

    @Test
    public void updateTest() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 1L);
        when(itemService.update(itemDto, 1L)).thenReturn(itemDto);
        mvc.perform(patch("/items/{id}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }

    @Test
    public void getTest() throws Exception {
        List<ItemWithBookingDto> items = List.of(new ItemWithBookingDto(1L, "name", "desc",
                true, 1L, null, null, null));
        when(itemService.get(1L, 0, 20)).thenReturn(items);
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(items)));
    }

    @Test
    public void getItemByIdTest() throws Exception {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "name", "desc",
                true, 1L, null, null, null);
        when(itemService.getItemById(1L, 1L)).thenReturn(itemWithBookingDto);
        mvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemWithBookingDto)));
    }

    @Test
    public void searchTest() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 1L);
        when(itemService.search("text", 0, 20)).thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search")
                        .queryParam("text", "text")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    public void postCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "comment", null, null, null, null);
        when(itemService.postComment(commentDto, 1L, 1L)).thenReturn(commentDto);
        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));
    }
}
