package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.model.compilation.CompilationDto;
import ru.practicum.explore_with_me.service.compilation.CompilationService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/compilations")
@Validated
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public Collection<CompilationDto> getAllCompilations(@RequestParam(value = "pinned") boolean pinned,
                                                         @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("/{id}")
    public CompilationDto getCompilationById(@PathVariable Long id) {
        return compilationService.getCompilationDtoOrThrow(id);
    }
}
