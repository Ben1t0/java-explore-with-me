package ru.practicum.explorewithme.statistic.service;

import org.springframework.stereotype.Service;

@Service
public class StatisticHttpClientService implements StatisticService {
    @Override
    public Long getStatistic(String endpoint) {
        //TODO
        if (endpoint.startsWith("/events/")) {
            return 10L;
        } else {
            return 0L;
        }
    }
}
