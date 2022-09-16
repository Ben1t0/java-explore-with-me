package ru.practicum.explore_with_me.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateRequestDto {
    private Long id;
    @NotNull
    private Long event;
}
