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

import java.util.List;
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
                        .map(eventService::getEvent)
                        .collect(Collectors.toList()))
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .build();
        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        getCompilation(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto deleteEventFromCompilation(Long compilationId, Long eventId) {
        Compilation compilation = getCompilation(compilationId);
        Event event = eventService.getEvent(eventId);
        compilation.getEvents().remove(event);

        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto addEventToCompilation(Long compilationId, Long eventId) {
        Compilation compilation = getCompilation(compilationId);
        Event event = eventService.getEvent(eventId);

        compilation.getEvents().add(event);

        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto unpinCompilation(Long compilationId) {
        Compilation compilation = getCompilation(compilationId);
        compilation.setPinned(false);
        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto pinCompilation(Long compilationId) {
        Compilation compilation = getCompilation(compilationId);
        compilation.setPinned(true);
        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getAll(boolean pinned, Integer from, Integer size) {
        return compilationRepository.findCompilationByPinnedIs(pinned, new OffsetBasedPageRequest(from, size)).stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationDto(long compilationId) {
        return CompilationMapper.toDto(getCompilation(compilationId));
    }

    private Compilation getCompilation(long id) {
        return compilationRepository.findById(id).orElseThrow(() -> new CompilationNotFoundException(id));
    }
}
