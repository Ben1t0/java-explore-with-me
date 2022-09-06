package ru.practicum.explorewithme.partisipationrequest.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {
    private Long event;
    private Long requester;
    private LocalDateTime created;
}
