package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookingService bookingService;

    @Test
    public void createTest() throws Exception {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), 1L, null, 1L, null, null);
        when(bookingService.create(bookingDto, 1L)).thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    public void setStatusTest() throws Exception {
        BookingDto bookingApproved = new BookingDto(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), 1L, null, 1L, null, Status.APPROVED);
        when(bookingService.setStatus(1L, 1L, true)).thenReturn(bookingApproved);
        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .queryParam("approved", "true")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingApproved)));
    }

    @Test
    public void getBookingByIdTest() throws Exception {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), 1L, null, 1L, null, null);
        when(bookingService.getBookingById(1L, 1L)).thenReturn(bookingDto);
        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    public void getAllBookingsByUserTest() throws Exception {
        List<BookingDto> bookings = List.of(new BookingDto(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), 1L, null, 1L, null, null));
        when(bookingService.getAllBookingsByUser(1L, State.ALL, 0, 20)).thenReturn(bookings);
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookings)));
    }

    @Test
    public void getAllBookingsForItemsBelongToUserTest() throws Exception {
        List<BookingDto> bookings = List.of(new BookingDto(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), 1L, null, 1L, null, null));
        when(bookingService.getAllBookingsForItemsBelongToUser(1L, State.ALL, 0, 20)).thenReturn(bookings);
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookings)));
    }
}
