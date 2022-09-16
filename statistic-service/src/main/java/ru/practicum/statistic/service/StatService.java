package ru.practicum.statistic.service;

import ru.practicum.statistic.model.HitDto;
import ru.practicum.statistic.model.StatDto;

import java.util.Collection;
import java.util.Set;

public interface StatService {

    HitDto create(HitDto dto);

    Collection<StatDto> findStat(long start, long end, Set<String> uris, boolean unique);
}
