package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.FullEventDto;
import ru.practicum.explorewithme.event.dto.ShortEventDto;
import ru.practicum.explorewithme.event.dto.SortType;
import ru.practicum.explorewithme.event.service.EventService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public Collection<ShortEventDto> getEvents(@RequestParam(value = "text", defaultValue = "") String text,
                                               @RequestParam(value = "categories") List<Long> catIds,
                                               @RequestParam(value = "paid") boolean paid,
                                               @RequestParam(value = "rangeStart", required = false)
                                                   LocalDateTime start,
                                               @RequestParam(value = "rangeEnd", required = false) LocalDateTime end,
                                               @RequestParam(value = "onlyAvailable") boolean onlyAvailable,
                                               @RequestParam(value = "sort") SortType sortType,
                                               @RequestParam(value = "from", defaultValue = "0") Integer from,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return eventService.find(text, catIds, paid, start, end, onlyAvailable, sortType, from, size);

    }

    @GetMapping("/{catId}")
    public FullEventDto getEventById(@PathVariable Long catId) {
        return eventService.getById(catId);
    }

}
