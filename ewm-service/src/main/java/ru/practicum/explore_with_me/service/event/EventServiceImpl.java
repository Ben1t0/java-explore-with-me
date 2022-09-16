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
    public Collection<ShortEventDto> findPublicEvent(String text, Collection<Long> catIds, boolean paid,
                                                     LocalDateTime start, LocalDateTime end, boolean onlyAvailable,
                                                     EventSortType sortType, Integer from, Integer size) {
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
                        eventDtos.add(dto);
                    }
                } else {
                    eventDtos.add(dto);
                }
            } else {
                eventDtos.add(dto);
            }
        }

        if (sortType == EventSortType.VIEWS) {
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
        Event event = getEventByIdOrThrow(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException(eventId);
        }
        return EventMapper.toFullDto(event);
    }

    @Override
    public Collection<ShortEventDto> getUserEvents(Long userId, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("id"));
        userService.getUserByIdOrThrow(userId);

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

        User user = userService.getUserByIdOrThrow(userId);

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
        LocalDateTime now = LocalDateTime.now();

        User user = userService.getUserByIdOrThrow(userId);
        Event eventToUpdate = getEventByIdOrThrow(createEventDto.getId()).toBuilder().build();

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
        User user = userService.getUserByIdOrThrow(userId);
        Event event = getEventByIdOrThrow(eventId);

        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(event.getId());
        }
        return EventMapper.toFullDto(event);
    }

    @Override
    public FullEventDto cancelUserEvent(Long userId, Long eventId) {
        User user = userService.getUserByIdOrThrow(userId);
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
    public Collection<FullEventDto> findEvents(Collection<Long> userIds, Collection<EventState> states,
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
    public FullEventDto adminUpdateEvent(Long eventId, AdminUpdateEventDto dto) {
        Event eventToUpdate = getEventByIdOrThrow(eventId).toBuilder().build();

        if (dto.getEventDate() != null) {
            eventToUpdate.setEventDate(dto.getEventDate());
        }

        if (dto.getAnnotation() != null && !dto.getAnnotation().isBlank()) {
            eventToUpdate.setAnnotation(dto.getAnnotation());
        }

        if (dto.getLocation() != null) {
            if (dto.getLocation().getLon() != null) {
                eventToUpdate.setLongitude(dto.getLocation().getLon());
            }
            if (dto.getLocation().getLat() != null) {
                eventToUpdate.setLatitude(dto.getLocation().getLat());
            }
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            eventToUpdate.setDescription(dto.getDescription());
        }

        if (dto.getRequestModeration() != null) {
            eventToUpdate.setRequestModeration(dto.getRequestModeration());
        }

        if (dto.getCategory() != null) {
            eventToUpdate.setCategory(categoryService.getCategoryByIdOrThrow(dto.getCategory()));
        }

        if (dto.getPaid() != null) {
            eventToUpdate.setPaid(dto.getPaid());
        }

        if (dto.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(dto.getParticipantLimit());
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            eventToUpdate.setTitle(dto.getTitle());
        }

        return EventMapper.toFullDto(eventRepository.save(eventToUpdate));
    }

    @Override
    public FullEventDto publishEvent(Long eventId) {
        LocalDateTime now = LocalDateTime.now();

        Event event = getEventByIdOrThrow(eventId);
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
        Event event = getEventByIdOrThrow(eventId);
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
    public Event getEventByIdOrThrow(long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }
}
