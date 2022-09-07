package ru.practicum.explorewithme.partisipationrequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.exception.EventBadRequestException;
import ru.practicum.explorewithme.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.partisipationrequest.dto.CreateRequestDto;
import ru.practicum.explorewithme.partisipationrequest.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.partisipationrequest.dto.RequestMapper;
import ru.practicum.explorewithme.partisipationrequest.exception.RequestNotFoundException;
import ru.practicum.explorewithme.partisipationrequest.model.ParticipationRequest;
import ru.practicum.explorewithme.partisipationrequest.model.ParticipationRequestStatus;
import ru.practicum.explorewithme.partisipationrequest.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.user.exception.UserNotActivatedException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.model.UserState;
import ru.practicum.explorewithme.user.service.UserService;

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
        User user = getActiveUserOrThrow(userId);
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new EventNotFoundException(eventId);
        }

        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto approveRequest(Long userId, Long eventId, Long reqId) {
        getActiveUserOrThrow(userId);
        ParticipationRequest request = getRequestOrThrow(reqId);
        if (!request.getEvent().getId().equals(eventId) || !request.getEvent().getInitiator().getId().equals(userId)) {
            throw new RequestNotFoundException(reqId);
        }
        request.setStatus(ParticipationRequestStatus.APPROVED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId) {
        getActiveUserOrThrow(userId);
        ParticipationRequest request = getRequestOrThrow(reqId);
        if (!request.getEvent().getId().equals(eventId) || !request.getEvent().getInitiator().getId().equals(userId)) {
            throw new RequestNotFoundException(reqId);
        }
        request.setStatus(ParticipationRequestStatus.REJECTED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public Collection<ParticipationRequestDto> getUserRequests(Long userId) {
        getActiveUserOrThrow(userId);

        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, CreateRequestDto createRequestDto) {
        LocalDateTime now = LocalDateTime.now();
        User user = getActiveUserOrThrow(userId);
        Event event = eventService.getEventByIdOrThrow(createRequestDto.getEvent());

        if (event.getInitiator().getId().equals(userId) || event.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException(event.getId());
        }

        if (event.getParticipantLimit() > 0 && event.getParticipationRequests().size() >= event.getParticipantLimit()) {
            throw new EventBadRequestException("Event is full");
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
        getActiveUserOrThrow(userId);
        ParticipationRequest request = getRequestOrThrow(reqId);
        if(!request.getRequester().getId().equals(userId)){
            throw new RequestNotFoundException(reqId);
        }
        request.setStatus(ParticipationRequestStatus.REJECTED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    private ParticipationRequest getRequestOrThrow(Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new RequestNotFoundException(id));
    }

    private User getActiveUserOrThrow(long id) {
        User user = userService.getUserByIdOrThrow(id);
        if (user.getState() == UserState.INACTIVE) {
            throw new UserNotActivatedException(id);
        }
        return user;
    }
}
