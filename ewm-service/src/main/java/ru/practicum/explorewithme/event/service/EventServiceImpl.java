package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.exception.EventBadRequestException;
import ru.practicum.explorewithme.event.exception.EventDateToEarlyException;
import ru.practicum.explorewithme.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.statistic.service.StatisticService;
import ru.practicum.explorewithme.user.exception.UserNotActivatedException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.model.UserState;
import ru.practicum.explorewithme.user.service.UserService;
import ru.practicum.explorewithme.utils.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final StatisticService statisticService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public Collection<ShortEventDto> findPublicEvent(String text, List<Long> catIds, boolean paid,
                                                     LocalDateTime start, LocalDateTime end, boolean onlyAvailable,
                                                     SortType sortType, Integer from, Integer size) {
        Collection<Event> events;
        Collection<ShortEventDto> eventDtos = new ArrayList<>();

        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            events = eventRepository.findAfterDate(text, catIds, paid, now);
        } else {
            events = eventRepository.findBetweenDates(text, catIds, paid, start, end);
        }

        for (Event event : events) {
            ShortEventDto dto = EventMapper.toShortDto(event);
            if (event.getParticipantLimit() > 0) {
                if (onlyAvailable) {
                    if (dto.getConfirmedRequests() < event.getParticipantLimit()) {
                        dto.setViews(statisticService.getStatistic("/events/" + event.getId()));
                        eventDtos.add(dto);
                    }
                } else {
                    dto.setViews(statisticService.getStatistic("/events/" + event.getId()));
                    eventDtos.add(dto);
                }
            } else {
                dto.setViews(statisticService.getStatistic("/events/" + event.getId()));
                eventDtos.add(dto);
            }
        }

        if (sortType == SortType.VIEWS) {
            return eventDtos.stream()
                    .sorted(Comparator.comparingLong(ShortEventDto::getViews))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } else {
            return eventDtos.stream()
                    .sorted(Comparator.comparing(ShortEventDto::getEventDate))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public FullEventDto getById(Long eventId) {
        FullEventDto responseDto = EventMapper.toFullDto(getEventByIdOrThrow(eventId));
        if (responseDto.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException(eventId);
        }
        responseDto.setViews(statisticService.getStatistic("/events/" + eventId));
        return responseDto;
    }

    @Override
    public Collection<ShortEventDto> getUserEvents(Long userId, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("id"));
        getActiveUserOrThrow(userId);

        return eventRepository.findAllByInitiatorId(userId, page).stream()
                .map(EventMapper::toShortDto)
                .peek(dto -> dto.setViews(statisticService.getStatistic("/events/" + dto.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public FullEventDto createEvent(Long userId, CreateEventDto createEventDto) {
        LocalDateTime now = LocalDateTime.now();
        if (createEventDto.getEventDate().isBefore(now.plusHours(2))) {
            throw new EventDateToEarlyException();
        }

        User user = getActiveUserOrThrow(userId);

        Category category = categoryService.getCategoryByIdOrThrow(createEventDto.getCategory());

        Event event = EventMapper.fromDto(createEventDto);
        event.setCreated(now);
        event.setInitiator(user);
        event.setState(EventState.PENDING);
        event.setCategory(category);

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public FullEventDto patchEvent(Long userId, CreateEventDto createEventDto) {
        User user = getActiveUserOrThrow(userId);
        Event eventToUpdate = getEventByIdOrThrow(createEventDto.getId()).toBuilder().build();

        if (!eventToUpdate.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(eventToUpdate.getId());
        }

        if (eventToUpdate.getState() == EventState.PUBLISHED) {
            throw new EventBadRequestException("Cant modify published event");
        }

        LocalDateTime now = LocalDateTime.now();
        if (createEventDto.getEventDate() != null && createEventDto.getEventDate().isBefore(now.plusHours(2))) {
            throw new EventDateToEarlyException();
        } else {
            eventToUpdate.setEventDate(createEventDto.getEventDate());
        }

        if (createEventDto.getAnnotation() != null && !createEventDto.getAnnotation().isBlank()) {
            eventToUpdate.setAnnotation(createEventDto.getAnnotation());
        }

        if (createEventDto.getDescription() != null && !createEventDto.getDescription().isBlank()) {
            eventToUpdate.setDescription(createEventDto.getDescription());
        }

        if (createEventDto.getCategory() != null) {
            eventToUpdate.setCategory(categoryService.getCategoryByIdOrThrow(createEventDto.getCategory()));
        }

        if (createEventDto.getPaid() != null) {
            eventToUpdate.setPaid(createEventDto.getPaid());
        }

        if (createEventDto.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(createEventDto.getParticipantLimit());
        }

        if (createEventDto.getTitle() != null && !createEventDto.getTitle().isBlank()) {
            eventToUpdate.setTitle(createEventDto.getTitle());
        }

        return EventMapper.toFullDto(eventRepository.save(eventToUpdate));
    }

    @Override
    public FullEventDto getUserEvent(Long userId, Long eventId) {
        User user = getActiveUserOrThrow(userId);
        Event event = getEventByIdOrThrow(eventId);

        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(event.getId());
        }

        FullEventDto responseDto = EventMapper.toFullDto(event);

        responseDto.setViews(statisticService.getStatistic("/events/" + eventId));
        return responseDto;
    }

    @Override
    public FullEventDto cancelUserEvent(Long userId, Long eventId) {
        User user = getActiveUserOrThrow(userId);
        Event eventToCancel = getEventByIdOrThrow(eventId).toBuilder().build();

        if (!eventToCancel.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(eventToCancel.getId());
        }

        if (eventToCancel.getState() == EventState.PUBLISHED) {
            throw new EventBadRequestException("Cant cancel published event");
        } else if (eventToCancel.getState() == EventState.CANCELED) {
            throw new EventBadRequestException("Event already canceled");
        }

        eventToCancel.setState(EventState.CANCELED);

        return EventMapper.toFullDto(eventRepository.save(eventToCancel));
    }

    @Override
    public Event getEventByIdOrThrow(long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    private User getActiveUserOrThrow(long id) {
        User user = userService.getUserByIdOrThrow(id);
        if (user.getState() == UserState.INACTIVE) {
            throw new UserNotActivatedException(id);
        }
        return user;
    }
}
