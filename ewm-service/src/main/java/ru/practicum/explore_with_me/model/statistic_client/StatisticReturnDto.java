package ru.practicum.explore_with_me.model.statistic_client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticReturnDto {
    private String app;
    private String uri;
    private long hits;
}
