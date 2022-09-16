package ru.practicum.explore_with_me.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.exception.EventBadRequestException;
import ru.practicum.explore_with_me.exception.EventDateToEarlyException;
import ru.practicum.explore_with_me.exception.EventNotFoundException;
import ru.practicum.explore_with_me.model.category.Category;
import ru.practicum.explore_with_me.model.event.*;
import ru.practicum.explore_with_me.model.user.User;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.service.category.CategoryService;
import ru.practicum.explore_with_me.service.statistic_client.StatisticService;
import ru.practicum.explore_with_me.service.user.UserService;
import ru.practicum.explore_with_me.utils.OffsetBasedPageRequest;

import javax.annotation.PostConstruct;
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
    private final UserService userService;
    private final CategoryService categoryService;
    private final StatisticService statisticService;

    @PostConstruct
    private void setStatisticServiceToEventMapper() {
        EventMapper.setStatisticService(statisticService);
    }

    @Override
    public List<ShortEventDto> findPublicEvent(String text, Collection<Long> categoryIds, boolean paid,
                                               LocalDateTime start, LocalDateTime end, boolean onlyAvailable,
                                               EventSortType eventSortType, Integer from, Integer size) {
        Collection<Event> events;
        Collection<ShortEventDto> eventDtos = new ArrayList<>();

        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            events = eventRepository.findAfterDate(text, categoryIds, paid, now);
        } else {
            events = eventRepository.findBetweenDates(text, categoryIds, paid, start, end);
        }

        for (Event event : events) {
            ShortEventDto dto = EventMapper.toShortDto(event);
            if (event.getParticipantLimit() > 0) {
                if (onlyAvailable) {
                    if (dto.getConfirmedRequests() < event.getParticipantLimit()) {
                        eventDtos.add(dto);
                    }
                } else {
                    eventDtos.add(dto);
                }
            } else {
                eventDtos.add(dto);
            }
        }

        if (eventSortType == EventSortType.VIEWS) {
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
        Event event = getEvent(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException(eventId);
        }
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<ShortEventDto> getUserEvents(Long userId, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("id"));
        userService.getUser(userId);

        return eventRepository.findAllByInitiatorId(userId, page).stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public FullEventDto createEvent(Long userId, CreateEventDto createEventDto) {
        LocalDateTime now = LocalDateTime.now();
        if (createEventDto.getEventDate().isBefore(now.plusHours(2))) {
            throw new EventDateToEarlyException();
        }

        User user = userService.getUser(userId);

        Category category = categoryService.getCategory(createEventDto.getCategory());

        Event event = EventMapper.fromDto(createEventDto);
        event.setCreated(now);
        event.setInitiator(user);
        event.setState(EventState.PENDING);
        event.setCategory(category);

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public FullEventDto patchEvent(Long userId, CreateEventDto createEventDto) {
        LocalDateTime now = LocalDateTime.now();

        User user = userService.getUser(userId);
        Event eventToUpdate = getEvent(createEventDto.getId()).toBuilder().build();

        if (!eventToUpdate.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(eventToUpdate.getId());
        }

        if (eventToUpdate.getState() == EventState.PUBLISHED) {
            throw new EventBadRequestException("Cant modify published event");
        }

        if (createEventDto.getEventDate() != null) {
            if (createEventDto.getEventDate().isBefore(now.plusHours(2))) {
                throw new EventDateToEarlyException();
            } else {
                eventToUpdate.setEventDate(createEventDto.getEventDate());
            }
        }

        if (createEventDto.getAnnotation() != null && !createEventDto.getAnnotation().isBlank()) {
            eventToUpdate.setAnnotation(createEventDto.getAnnotation());
        }

        if (createEventDto.getDescription() != null && !createEventDto.getDescription().isBlank()) {
            eventToUpdate.setDescription(createEventDto.getDescription());
        }

        if (createEventDto.getCategory() != null) {
            eventToUpdate.setCategory(categoryService.getCategory(createEventDto.getCategory()));
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
        User user = userService.getUser(userId);
        Event event = getEvent(eventId);

        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(event.getId());
        }
        return EventMapper.toFullDto(event);
    }

    @Override
    public FullEventDto cancelUserEvent(Long userId, Long eventId) {
        User user = userService.getUser(userId);
        Event eventToCancel = getEvent(eventId).toBuilder().build();

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
    public List<FullEventDto> findEvents(Collection<Long> userIds, Collection<EventState> states,
                                         Collection<Long> catIds, LocalDateTime start, LocalDateTime end,
                                         Integer from, Integer size) {

        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("id"));
        Collection<Event> events = eventRepository.findBetweenDatesByUsersStatesCategories(userIds, states, catIds,
                start, end, page);

        return events.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public FullEventDto adminUpdateEvent(Long eventId, AdminUpdateEventDto adminUpdateEventDto) {
        Event eventToUpdate = getEvent(eventId).toBuilder().build();

        if (adminUpdateEventDto.getEventDate() != null) {
            eventToUpdate.setEventDate(adminUpdateEventDto.getEventDate());
        }

        if (adminUpdateEventDto.getAnnotation() != null && !adminUpdateEventDto.getAnnotation().isBlank()) {
            eventToUpdate.setAnnotation(adminUpdateEventDto.getAnnotation());
        }

        if (adminUpdateEventDto.getLocation() != null) {
            if (adminUpdateEventDto.getLocation().getLon() != null) {
                eventToUpdate.setLongitude(adminUpdateEventDto.getLocation().getLon());
            }
            if (adminUpdateEventDto.getLocation().getLat() != null) {
                eventToUpdate.setLatitude(adminUpdateEventDto.getLocation().getLat());
            }
        }

        if (adminUpdateEventDto.getDescription() != null && !adminUpdateEventDto.getDescription().isBlank()) {
            eventToUpdate.setDescription(adminUpdateEventDto.getDescription());
        }

        if (adminUpdateEventDto.getRequestModeration() != null) {
            eventToUpdate.setRequestModeration(adminUpdateEventDto.getRequestModeration());
        }

        if (adminUpdateEventDto.getCategory() != null) {
            eventToUpdate.setCategory(categoryService.getCategory(adminUpdateEventDto.getCategory()));
        }

        if (adminUpdateEventDto.getPaid() != null) {
            eventToUpdate.setPaid(adminUpdateEventDto.getPaid());
        }

        if (adminUpdateEventDto.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(adminUpdateEventDto.getParticipantLimit());
        }

        if (adminUpdateEventDto.getTitle() != null && !adminUpdateEventDto.getTitle().isBlank()) {
            eventToUpdate.setTitle(adminUpdateEventDto.getTitle());
        }

        return EventMapper.toFullDto(eventRepository.save(eventToUpdate));
    }

    @Override
    public FullEventDto publishEvent(Long eventId) {
        LocalDateTime now = LocalDateTime.now();

        Event event = getEvent(eventId);
        if (event.getEventDate().isBefore(now.plusHours(1))) {
            throw new EventBadRequestException("Event starts in less than 1 hour");
        }
        if (event.getState() == EventState.CANCELED) {
            throw new EventBadRequestException("Event was rejected");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new EventBadRequestException("Event already published");
        }
        event.setState(EventState.PUBLISHED);
        event.setPublished(now);

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public FullEventDto rejectEvent(Long eventId) {
        Event event = getEvent(eventId);
        if (event.getState() == EventState.CANCELED) {
            throw new EventBadRequestException("Event already rejected");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new EventBadRequestException("Event was published");
        }
        event.setState(EventState.CANCELED);
        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public Event getEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
    }
}
