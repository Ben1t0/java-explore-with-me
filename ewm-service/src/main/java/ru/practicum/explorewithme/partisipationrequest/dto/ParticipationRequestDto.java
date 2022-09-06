package ru.practicum.explorewithme.partisipationrequest.dto;


import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.partisipationrequest.model.ParticipationRequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private LocalDateTime created;
    private ParticipationRequestStatus status;
}
