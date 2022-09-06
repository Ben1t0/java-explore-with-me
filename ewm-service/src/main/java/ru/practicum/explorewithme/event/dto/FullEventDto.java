package ru.practicum.explorewithme.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.explorewithme.event.model.EventState;

import java.time.LocalDateTime;

@Getter
@Builder
public class FullEventDto {
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Long id;
    private User initiator;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    @Setter
    private Long views;

    @AllArgsConstructor
    @Getter
    public static class Category {
        private Long id;
        private String name;
    }

    @AllArgsConstructor
    @Getter
    public static class User {
        private Long id;
        private String name;
    }
}
