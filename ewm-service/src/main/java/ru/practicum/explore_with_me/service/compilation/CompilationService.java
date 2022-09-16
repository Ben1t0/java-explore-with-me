package ru.practicum.explore_with_me.service.compilation;

import ru.practicum.explore_with_me.model.compilation.CompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(CompilationDto compilationDto);

    void deleteCompilation(Long compilationId);

    CompilationDto deleteEventFromCompilation(Long compilationId, Long eventId);

    CompilationDto addEventToCompilation(Long compilationId, Long eventId);

    CompilationDto unpinCompilation(Long compilationId);

    CompilationDto pinCompilation(Long compilationId);

    CompilationDto getCompilationDto(long compilationId);

    List<CompilationDto> getAll(boolean pinned, Integer from, Integer size);
}
