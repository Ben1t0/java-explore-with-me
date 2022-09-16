package ru.practicum.statistic.service;

import ru.practicum.statistic.model.HitDto;
import ru.practicum.statistic.model.StatDto;

import java.util.List;
import java.util.Set;

public interface StatService {

    HitDto create(HitDto dto);

    List<StatDto> findStat(long start, long end, Set<String> uris, boolean unique);
}
