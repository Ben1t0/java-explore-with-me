package ru.practicum.explorewithme.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.event.dto.CreateEventDto;
import ru.practicum.explorewithme.event.service.EventService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private EventService eventService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    CreateEventDto createEventDto;

    @BeforeEach
    void setUp() {
        createEventDto = CreateEventDto.builder()
                .annotation("annot")
                .category(1L)
                .description("decr")
                .eventDate(LocalDateTime.now().plusDays(1))
                .location(CreateEventDto.Location.builder().lan(55.3f).lon(13.54f).build())
                .participantLimit(1500)
                .paid(true)
                .title("title")
                .build();
    }

    @Test
    void createEventTest() throws Exception {

        mvc.perform(post("/users/1/events")
                        .content(mapper.writeValueAsString(createEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(eventService, Mockito.times(1)).createEvent(1L, createEventDto);
    }
}