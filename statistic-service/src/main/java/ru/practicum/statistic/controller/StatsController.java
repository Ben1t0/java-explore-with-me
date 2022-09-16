package ru.practicum.statistic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistic.model.HitDto;
import ru.practicum.statistic.model.StatDto;
import ru.practicum.statistic.service.StatService;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatService statService;

    @PostMapping("/hit")
    public HitDto createHit(@RequestBody HitDto dto) {
        return statService.create(dto);
    }

    @GetMapping("/stats")
    public List<StatDto> getStats(@RequestParam(value = "start") long start,
                                  @RequestParam(value = "end") long end,
                                  @RequestParam(value = "uris") Set<String> uris,
                                  @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        return statService.findStat(start, end, uris, unique);
    }
}
