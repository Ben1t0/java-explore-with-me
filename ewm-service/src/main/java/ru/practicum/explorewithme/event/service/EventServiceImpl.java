package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.dto.EventMapper;
import ru.practicum.explorewithme.event.dto.FullEventDto;
import ru.practicum.explorewithme.event.dto.ShortEventDto;
import ru.practicum.explorewithme.event.dto.SortType;
import ru.practicum.explorewithme.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.statistic.service.StatisticService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final StatisticService statisticService;

    @Override
    public Collection<ShortEventDto> find(String text, List<Long> eventIds, boolean paid,
                                          LocalDateTime start, LocalDateTime end, boolean onlyAvailable,
                                          SortType sortType, Integer from, Integer size) {


        return null;
    }

    @Override
    public FullEventDto getById(Long eventId) {
        FullEventDto responseDto = EventMapper.toFullDto(getUserByIdOrThrow(eventId));
        responseDto.setViews(statisticService.getStatistic("/events/" + eventId));
        return responseDto;
    }

    private Event getUserByIdOrThrow(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }
}
