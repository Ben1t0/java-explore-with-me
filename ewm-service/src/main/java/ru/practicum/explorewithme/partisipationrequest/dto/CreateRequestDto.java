package ru.practicum.explorewithme.partisipationrequest.dto;

import javax.validation.constraints.NotNull;

public class CreateRequestDto {
    private Long id;
    @NotNull
    private Long event;
    @NotNull
    private Long requester;
}
