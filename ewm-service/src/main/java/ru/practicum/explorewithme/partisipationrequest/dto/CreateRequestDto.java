package ru.practicum.explorewithme.partisipationrequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateRequestDto {
    private Long id;
    @NotNull
    private Long event;
}
