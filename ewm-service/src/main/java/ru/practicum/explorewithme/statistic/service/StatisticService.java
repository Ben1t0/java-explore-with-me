package ru.practicum.explorewithme.statistic.service;

public interface StatisticService {
    Long getStatistic(String endpoint);

    void hitEndpoint(String endpoint, String ipAddress);
}
