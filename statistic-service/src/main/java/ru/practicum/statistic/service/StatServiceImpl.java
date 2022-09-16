package ru.practicum.statistic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statistic.model.HitDto;
import ru.practicum.statistic.model.StatDto;
import ru.practicum.statistic.model.EndpointHit;
import ru.practicum.statistic.repository.HitRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatServiceImpl implements StatService {
    private final HitRepository repository;

    @Override
    public HitDto create(HitDto dto) {
        EndpointHit hit = EndpointHit.builder()
                .timestamp(Instant.now().getEpochSecond())
                .ip(dto.getIp())
                .app(dto.getApp())
                .uri(dto.getUri())
                .build();

        return HitDto.from(repository.save(hit));
    }

    @Override
    public Collection<StatDto> findStat(long start, long end, Set<String> uris, boolean unique) {
        //Map<String, List<EndpointHit>> hits = repository.findAllByUriInAndTimestampBetween(uris, start, end).stream()
        //                .collect(Collectors.groupingBy(EndpointHit::getUri));

       /*var list = repository.findAllByUriInAndTimestampBetween(uris, start, end).stream()
                .collect(Collectors.groupingBy(EndpointHit::getUri)).values().stream()
                .map(hits -> {
                    return StatDto.builder()
                            .uri(hits.get(0).getUri())
                            .app(hits.get(0).getApp())
                            .hits(unique ? hits.stream().filter(distinctByKey(EndpointHit::getIp)).count() : hits.size())
                            .build();
                }).collect(Collectors.toList());*/

        /*Collection<StatDto> stats = new ArrayList<>();


        for (List<EndpointHit> entry : hits.values()) {
            stats.add(StatDto.builder()
                    .uri(entry.get(0).getUri())
                    .app(entry.get(0).getApp())
                    .hits(unique ? entry.stream().filter(distinctByKey(EndpointHit::getIp)).count() : entry.size())
                    .build());
        }*/


        return repository.findAllByUriInAndTimestampBetween(uris, start, end).stream()
                .collect(Collectors.groupingBy(EndpointHit::getUri)).values().stream()
                .map(hits -> StatDto.builder()
                        .uri(hits.get(0).getUri())
                        .app(hits.get(0).getApp())
                        .hits(unique ? hits.stream().filter(distinctByKey(EndpointHit::getIp)).count() : hits.size())
                        .build())
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
