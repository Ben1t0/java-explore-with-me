package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.dto.CreateEventDto;
import ru.practicum.explorewithme.event.dto.FullEventDto;
import ru.practicum.explorewithme.event.dto.ShortEventDto;
import ru.practicum.explorewithme.event.dto.SortType;
import ru.practicum.explorewithme.event.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService  {
    Collection<ShortEventDto> findPublicEvent(String text, List<Long> catIds, boolean paid,
                                              LocalDateTime start, LocalDateTime end, boolean onlyAvailable,
                                              SortType sortType, Integer from, Integer size);

    FullEventDto getById(Long eventId);

    Collection<ShortEventDto> getUserEvents(Long userId, Integer from, Integer size);

    FullEventDto createEvent(Long userId, CreateEventDto createEventDto);

    FullEventDto patchEvent(Long userId, CreateEventDto createEventDto);

    FullEventDto getUserEvent(Long userId, Long eventId);

    FullEventDto cancelUserEvent(Long userId, Long eventId);

    Event getEventByIdOrThrow(long id);
}
