package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.dto.FullEventDto;
import ru.practicum.explorewithme.event.dto.ShortEventDto;
import ru.practicum.explorewithme.event.dto.SortType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {
    Collection<ShortEventDto> find(String text, List<Long> eventIds, boolean paid,
                                   LocalDateTime start, LocalDateTime end, boolean onlyAvailable,
                                   SortType sortType, Integer from, Integer size);

    FullEventDto getById(Long eventId);
}
