package ru.practicum.explorewithme.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.validation.Validation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
public class CreateEventDto {
    @NotNull(groups = Validation.OnCreate.class)
    @NotBlank(groups = Validation.OnCreate.class)
    private String annotation;
    @NotNull(groups = Validation.OnCreate.class)
    private Long category;
    @NotNull(groups = Validation.OnCreate.class)
    @NotBlank(groups = Validation.OnCreate.class)
    private String description;
    @NotNull(groups = Validation.OnCreate.class)
    private LocalDateTime eventDate;
    @NotNull(groups = Validation.OnCreate.class)
    private Location location;
    @NotNull(groups = Validation.OnCreate.class)
    private Boolean paid;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @NotNull(groups = Validation.OnCreate.class)
    private String title;

    @Builder
    public static class Location {
        private String name;
        private Long id;
    }
}
