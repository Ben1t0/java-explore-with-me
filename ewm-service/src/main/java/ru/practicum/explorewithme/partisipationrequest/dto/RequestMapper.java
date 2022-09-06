package ru.practicum.explorewithme.partisipationrequest.dto;

import ru.practicum.explorewithme.partisipationrequest.model.ParticipationRequest;

public class RequestMapper {
    public static ParticipationRequestDto toDto(ParticipationRequest request){
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }
}
