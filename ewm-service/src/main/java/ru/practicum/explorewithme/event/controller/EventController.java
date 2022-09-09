package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.FullEventDto;
import ru.practicum.explorewithme.event.dto.ShortEventDto;
import ru.practicum.explorewithme.event.dto.SortType;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.statistic.service.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
@Slf4j
public class EventController {
    private final EventService eventService;
    private final StatisticService statisticService;

    @GetMapping
    public Collection<ShortEventDto> getEvents(@RequestParam(value = "text", defaultValue = "") String text,
                                               @RequestParam(value = "categories") Set<Long> catIds,
                                               @RequestParam(value = "paid") boolean paid,
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                               @RequestParam(value = "rangeStart", required = false)
                                               LocalDateTime start,
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                               @RequestParam(value = "rangeEnd", required = false) LocalDateTime end,
                                               @RequestParam(value = "onlyAvailable") boolean onlyAvailable,
                                               @RequestParam(value = "sort") SortType sortType,
                                               @RequestParam(value = "from", defaultValue = "0") Integer from,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                                               HttpServletRequest request) {

        Collection<ShortEventDto> response = eventService.findPublicEvent(text, catIds, paid, start, end,
                onlyAvailable, sortType, from, size);
        try {
            statisticService.hitEndpoint(request.getRequestURI(), request.getRemoteAddr());
        } catch (Exception ex) {
            log.error("Statistic service POST error: " + ex.getMessage());
        }

        return response;
    }

    @GetMapping("/{catId}")
    public FullEventDto getEventById(@PathVariable Long catId, HttpServletRequest request) {
        FullEventDto response = eventService.getById(catId);
        try {
            statisticService.hitEndpoint(request.getRequestURI(), request.getRemoteAddr());
        } catch (Exception ex) {
            log.error("Statistic service POST error: " + ex.getMessage());
        }
        return response;
    }

}
