package ru.practicum.statistic.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.statistic.model.EndpointHit;

@Data
@Builder
public class HitDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private Long timestamp;

    public static HitDto from(EndpointHit hit) {
        return HitDto.builder()
                .app(hit.getApp())
                .id(hit.getId())
                .uri(hit.getUri())
                .timestamp(hit.getTimestamp())
                .ip(hit.getIp())
                .build();
    }
}
