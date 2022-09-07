package ru.practicum.explorewithme.compilation.service;

import ru.practicum.explorewithme.compilation.dto.CompilationDto;

import java.util.Collection;

public interface CompilationService {
    CompilationDto createCompilation(CompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto deleteEventFromCompilation(Long compId, Long eventId);

    CompilationDto addEventToCompilation(Long compId, Long eventId);

    CompilationDto unpinCompilation(Long compId);

    CompilationDto pinCompilation(Long compId);

    CompilationDto getCompilationDtoOrThrow(long id);

    Collection<CompilationDto> getAll(boolean pinned, Integer from, Integer size);
}
