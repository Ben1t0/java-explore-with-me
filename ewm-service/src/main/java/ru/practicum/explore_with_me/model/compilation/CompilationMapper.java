package ru.practicum.explore_with_me.model.compilation;

import ru.practicum.explore_with_me.model.event.Event;

import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList()))
                .build();
    }
}
