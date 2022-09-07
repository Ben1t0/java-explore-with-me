package ru.practicum.explorewithme.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShortEventDto {

    private String annotation;
    private Category category;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private Long id;
    private User initiator;
    private boolean paid;
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
