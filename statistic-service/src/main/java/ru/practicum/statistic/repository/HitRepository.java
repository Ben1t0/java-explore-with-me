package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.statistic.model.EndpointHit;

import java.util.Collection;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {
    Collection<EndpointHit> findAllByUriInAndTimestampBetween(Collection<String> uris, long start, long end);
}
