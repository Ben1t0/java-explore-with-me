package ru.practicum.explore_with_me.model.request;


import lombok.Builder;
import lombok.Data;

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
