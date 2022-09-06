package ru.practicum.explorewithme.partisipationrequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.partisipationrequest.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.partisipationrequest.dto.RequestMapper;
import ru.practicum.explorewithme.partisipationrequest.exception.RequestNotFoundException;
import ru.practicum.explorewithme.partisipationrequest.model.ParticipationRequest;
import ru.practicum.explorewithme.partisipationrequest.model.ParticipationRequestStatus;
import ru.practicum.explorewithme.partisipationrequest.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventService eventService;
    private final UserService userService;

    @Override
    public Collection<ParticipationRequestDto> getUserRequests(Long userId, Long eventId) {
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
        ParticipationRequest request = getRequestOrThrow(reqId);
        if (!request.getEvent().getId().equals(eventId) || !request.getEvent().getInitiator().getId().equals(userId)) {
            throw new RequestNotFoundException(reqId);
        }
        request.setStatus(ParticipationRequestStatus.APPROVED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId) {
        ParticipationRequest request = getRequestOrThrow(reqId);
        if (!request.getEvent().getId().equals(eventId) || !request.getEvent().getInitiator().getId().equals(userId)) {
            throw new RequestNotFoundException(reqId);
        }
        request.setStatus(ParticipationRequestStatus.REJECTED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    private ParticipationRequest getRequestOrThrow(Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new RequestNotFoundException(id));
    }

}
