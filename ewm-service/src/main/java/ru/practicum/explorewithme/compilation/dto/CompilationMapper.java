package ru.practicum.explorewithme.compilation.dto;

import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.event.model.Event;

import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList()))
                .build();
    }
}
