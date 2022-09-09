package ru.practicum.statistic.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.statistic.model.EndpointHit;

@Data
@Builder
public class StatDto {
    private String app;
    private String uri;
    private long hits;

    public static StatDto from(EndpointHit hit) {
        return StatDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .build();
    }
}
