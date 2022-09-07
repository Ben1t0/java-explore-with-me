package ru.practicum.explorewithme.partisipationrequest.service;

import ru.practicum.explorewithme.partisipationrequest.dto.CreateRequestDto;
import ru.practicum.explorewithme.partisipationrequest.dto.ParticipationRequestDto;

import java.util.Collection;

public interface ParticipationRequestService {
    Collection<ParticipationRequestDto> getRequestsForUserEvents(Long userId, Long eventId);

    ParticipationRequestDto approveRequest(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId);

    Collection<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto createRequest(Long userId, CreateRequestDto createRequestDto);

    ParticipationRequestDto cancelRequest(Long userId, Long reqId);
}
