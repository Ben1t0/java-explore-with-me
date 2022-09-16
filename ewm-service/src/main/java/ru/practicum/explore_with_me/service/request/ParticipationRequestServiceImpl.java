package ru.practicum.explore_with_me.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.exception.EventBadRequestException;
import ru.practicum.explore_with_me.exception.EventNotFoundException;
import ru.practicum.explore_with_me.exception.RequestNotFoundException;
import ru.practicum.explore_with_me.model.event.Event;
import ru.practicum.explore_with_me.model.event.EventState;
import ru.practicum.explore_with_me.model.request.*;
import ru.practicum.explore_with_me.model.user.User;
import ru.practicum.explore_with_me.repository.ParticipationRequestRepository;
import ru.practicum.explore_with_me.service.event.EventService;
import ru.practicum.explore_with_me.service.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventService eventService;
    private final UserService userService;

    @Override
    public Collection<ParticipationRequestDto> getRequestsForUserEvents(Long userId, Long eventId) {
        Event event = eventService.getEventByIdOrThrow(eventId);
        User user = userService.getUserByIdOrThrow(userId);
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(eventId);
        }

        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto approveRequest(Long userId, Long eventId, Long reqId) {
        userService.getUserByIdOrThrow(userId);
        ParticipationRequest request = getRequestOrThrow(reqId);
        if (!request.getEvent().getId().equals(eventId) || !request.getEvent().getInitiator().getId().equals(userId)) {
            throw new RequestNotFoundException(reqId);
        }

        if (request.getEvent().getParticipantLimit() > 0) {
            long confirmedRequests = request.getEvent().getParticipationRequests().stream()
                    .filter(r -> r.getStatus() == ParticipationRequestStatus.APPROVED)
                    .count();
            if (confirmedRequests >= request.getEvent().getParticipantLimit()) {
                throw new EventBadRequestException("Event participant limit reached");
            } else if (confirmedRequests == request.getEvent().getParticipantLimit() - 1) {
                request.getEvent().getParticipationRequests().stream()
                        .filter(r -> !r.getId().equals(reqId) && r.getStatus() == ParticipationRequestStatus.PENDING)
                        .peek(r -> r.setStatus(ParticipationRequestStatus.REJECTED))
                        .peek(requestRepository::save);
            }
        }
        request.setStatus(ParticipationRequestStatus.APPROVED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId) {
        userService.getUserByIdOrThrow(userId);
        ParticipationRequest request = getRequestOrThrow(reqId);
        if (!request.getEvent().getId().equals(eventId) || !request.getEvent().getInitiator().getId().equals(userId)) {
            throw new RequestNotFoundException(reqId);
        }
        request.setStatus(ParticipationRequestStatus.REJECTED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public Collection<ParticipationRequestDto> getUserRequests(Long userId) {
        userService.getUserByIdOrThrow(userId);

        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, CreateRequestDto createRequestDto) {
        LocalDateTime now = LocalDateTime.now();
        User user = userService.getUserByIdOrThrow(userId);
        Event event = eventService.getEventByIdOrThrow(createRequestDto.getEvent());

        if (event.getInitiator().getId().equals(userId) || event.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException(event.getId());
        }

        if (event.getParticipantLimit() > 0 && event.getParticipationRequests().stream()
                .filter(r -> r.getStatus() == ParticipationRequestStatus.APPROVED)
                .count() >= event.getParticipantLimit()) {
            throw new EventBadRequestException("Event participant limit reached");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .requester(user)
                .created(now)
                .event(event)
                .build();

        if (event.isRequestModeration()) {
            request.setStatus(ParticipationRequestStatus.PENDING);
        } else {
            request.setStatus(ParticipationRequestStatus.APPROVED);
        }

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long reqId) {
        userService.getUserByIdOrThrow(userId);
        ParticipationRequest request = getRequestOrThrow(reqId);
        if (!request.getRequester().getId().equals(userId)) {
            throw new RequestNotFoundException(reqId);
        }
        request.setStatus(ParticipationRequestStatus.REJECTED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    private ParticipationRequest getRequestOrThrow(Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new RequestNotFoundException(id));
    }
}
