package ru.practicum.explorewithme.statistic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.statistic.client.StatisticClient;
import ru.practicum.explorewithme.statistic.dto.HitDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticHttpClientService implements StatisticService {

    private final StatisticClient client;

    private static final String SERVICE_NAME = "Explore-With-Me";

    @Override
    public void hitEndpoint(String endpoint, String ipAddress) {
        HitDto dto = HitDto.builder()
                .app(SERVICE_NAME)
                .uri(endpoint)
                .ip(ipAddress)
                .build();

        client.post("hit", dto);
    }

    @Override
    public Long getStatistic(String endpoint) {
        Long startRange = 0L;
        Long endRange = LocalDateTime.now().plusYears(20).toEpochSecond(ZoneOffset.UTC);

        Map<String, Object> parameters = Map.of(
                "start", startRange,
                "end", endRange,
                "uris", endpoint
        );

        ResponseEntity<Object> response;

        try {
            response = client.get("stats?start={start}&end={end}&uris={uris}", parameters);
        } catch (Exception ex) {
            log.error("Statistic service GET error: " + ex.getMessage());
            return null;
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> stats = (List<Map<String, Object>>) response.getBody();
            if (stats != null && stats.size() > 0) {
                return ((Number) stats.get(0).get("hits")).longValue();
            }
        }
        return null;
    }
}
