package ru.practicum.explore_with_me.service.event;

import ru.practicum.explore_with_me.model.event.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {
    List<ShortEventDto> findPublicEvent(String text, Collection<Long> categoryIds, boolean paid,
                                        LocalDateTime start, LocalDateTime end, boolean onlyAvailable,
                                        EventSortType eventSortType, Integer from, Integer size);

    FullEventDto getById(Long eventId);

    List<ShortEventDto> getUserEvents(Long userId, Integer from, Integer size);

    FullEventDto createEvent(Long userId, CreateEventDto createEventDto);

    FullEventDto patchEvent(Long userId, CreateEventDto createEventDto);

    FullEventDto getUserEvent(Long userId, Long eventId);

    FullEventDto cancelUserEvent(Long userId, Long eventId);

    Event getEvent(long eventId);

    List<FullEventDto> findEvents(Collection<Long> userIds, Collection<EventState> states,
                                  Collection<Long> categoryIds, LocalDateTime start, LocalDateTime end, Integer from,
                                  Integer size);

    FullEventDto adminUpdateEvent(Long eventId, AdminUpdateEventDto adminUpdateEventDto);

    FullEventDto publishEvent(Long eventId);

    FullEventDto rejectEvent(Long eventId);
}
