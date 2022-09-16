package ru.practicum.explore_with_me.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.exception.CompilationNotFoundException;
import ru.practicum.explore_with_me.model.compilation.Compilation;
import ru.practicum.explore_with_me.model.compilation.CompilationDto;
import ru.practicum.explore_with_me.model.compilation.CompilationMapper;
import ru.practicum.explore_with_me.model.event.Event;
import ru.practicum.explore_with_me.repository.CompilationRepository;
import ru.practicum.explore_with_me.service.event.EventService;
import ru.practicum.explore_with_me.utils.OffsetBasedPageRequest;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    @Override
    public CompilationDto createCompilation(CompilationDto compilationDto) {
        Compilation compilation = Compilation.builder()
                .events(compilationDto.getEvents().stream()
                        .map(eventService::getEventByIdOrThrow)
                        .collect(Collectors.toList()))
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .build();
        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        getCompilationOrThrow(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilationOrThrow(compId);
        Event event = eventService.getEventByIdOrThrow(eventId);
        compilation.getEvents().remove(event);

        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilationOrThrow(compId);
        Event event = eventService.getEventByIdOrThrow(eventId);

        compilation.getEvents().add(event);

        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto unpinCompilation(Long compId) {
        Compilation compilation = getCompilationOrThrow(compId);
        compilation.setPinned(false);
        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto pinCompilation(Long compId) {
        Compilation compilation = getCompilationOrThrow(compId);
        compilation.setPinned(true);
        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public Collection<CompilationDto> getAll(boolean pinned, Integer from, Integer size) {
        return compilationRepository.findCompilationByPinnedIs(pinned, new OffsetBasedPageRequest(from, size)).stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationDtoOrThrow(long id) {
        return CompilationMapper.toDto(getCompilationOrThrow(id));
    }

    private Compilation getCompilationOrThrow(long id) {
        return compilationRepository.findById(id).orElseThrow(() -> new CompilationNotFoundException(id));
    }
}
