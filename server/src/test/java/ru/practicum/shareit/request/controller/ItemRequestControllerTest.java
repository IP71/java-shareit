package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    public void createTest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "request", 1L, null, null);
        when(itemRequestService.create(itemRequestDto, 1L)).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
    }

    @Test
    public void getUserRequestsTest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "request", 1L, null, null);
        when(itemRequestService.getUserRequests(1L)).thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto))));
    }

    @Test
    public void getAllUsersRequestsTest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "request", 1L, null, null);
        when(itemRequestService.getAllUsersRequests(1L, 0, 20)).thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto))));
    }

    @Test
    public void getRequestByIdTest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "request", 1L, null, null);
        when(itemRequestService.getRequestById(1L, 1L)).thenReturn(itemRequestDto);
        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
    }
}
