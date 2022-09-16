package ru.practicum.explore_with_me.service.event;

import ru.practicum.explore_with_me.model.event.*;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventService {
    Collection<ShortEventDto> findPublicEvent(String text, Collection<Long> catIds, boolean paid,
                                              LocalDateTime start, LocalDateTime end, boolean onlyAvailable,
                                              EventSortType sortType, Integer from, Integer size);

    FullEventDto getById(Long eventId);

    Collection<ShortEventDto> getUserEvents(Long userId, Integer from, Integer size);

    FullEventDto createEvent(Long userId, CreateEventDto createEventDto);

    FullEventDto patchEvent(Long userId, CreateEventDto createEventDto);

    FullEventDto getUserEvent(Long userId, Long eventId);

    FullEventDto cancelUserEvent(Long userId, Long eventId);

    Event getEventByIdOrThrow(long id);

    Collection<FullEventDto> findEvents(Collection<Long> userIds, Collection<EventState> states,
                                        Collection<Long> catIds, LocalDateTime start, LocalDateTime end, Integer from,
                                        Integer size);

    FullEventDto adminUpdateEvent(Long eventId, AdminUpdateEventDto dto);

    FullEventDto publishEvent(Long eventId);

    FullEventDto rejectEvent(Long eventId);
}