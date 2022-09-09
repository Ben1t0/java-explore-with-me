package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.validation.Validation;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateEventDto {
    @NotNull(groups = Validation.OnPatch.class)
    private Long id;
    @NotNull(groups = Validation.OnCreate.class)
    @NotBlank(groups = Validation.OnCreate.class)
    private String annotation;
    @NotNull(groups = Validation.OnCreate.class)
    private Long category;
    @NotNull(groups = Validation.OnCreate.class)
    @NotBlank(groups = Validation.OnCreate.class)
    private String description;
    @NotNull(groups = Validation.OnCreate.class)
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull(groups = Validation.OnCreate.class)
    private Location location;
    @NotNull(groups = Validation.OnCreate.class)
    private Boolean paid;
    @PositiveOrZero
    @Builder.Default
    private Integer participantLimit = 0;
    @Builder.Default
    private Boolean requestModeration = true;
    @NotNull(groups = Validation.OnCreate.class)
    private String title;

    @Builder
    @Data
    public static class Location {
        @NotNull(groups = Validation.OnCreate.class)
        private Float lat;
        @NotNull(groups = Validation.OnCreate.class)
        private Float lon;
    }
}
