package ru.practicum.explorewithme.event.dto;

import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.partisipationrequest.model.ParticipationRequestStatus;

public class EventMapper {
    public static FullEventDto toFullDto(Event event) {
        return FullEventDto.builder()
                .id(event.getId())
                .category(new FullEventDto.Category(event.getCategory().getId(), event.getCategory().getName()))
                .eventDate(event.getEventDate())
                .annotation(event.getAnnotation())
                .createdOn(event.getCreated())
                .publishedOn(event.getPublished())
                .description(event.getDescription())
                .title(event.getTitle())
                .paid(event.isPaid())
                .requestModeration(event.isRequestModeration())
                .initiator(new FullEventDto.User(event.getInitiator().getId(), event.getInitiator().getName()))
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .confirmedRequests(event.getParticipationRequests().stream()
                        .filter(r -> r.getStatus() == ParticipationRequestStatus.APPROVED).count())
                .build();
    }

    public static ShortEventDto toShortDto(Event event) {
        return ShortEventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new ShortEventDto.Category(event.getCategory().getId(), event.getCategory().getName()))
                .eventDate(event.getEventDate())
                .initiator(new ShortEventDto.User(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .confirmedRequests(event.getParticipationRequests().stream()
                        .filter(r -> r.getStatus() == ParticipationRequestStatus.APPROVED).count())
                .build();
    }

    public static Event fromDto(CreateEventDto createEventDto) {
        return Event.builder()
                .annotation(createEventDto.getAnnotation())
                .description(createEventDto.getDescription())
                .eventDate(createEventDto.getEventDate())
                .paid(createEventDto.getPaid())
                .participantLimit(createEventDto.getParticipantLimit())
                .requestModeration(createEventDto.getRequestModeration())
                .title(createEventDto.getTitle())
                .build();
    }
}
