package ru.practicum.explorewithme.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.CompilationMapper;
import ru.practicum.explorewithme.compilation.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.utils.OffsetBasedPageRequest;

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
