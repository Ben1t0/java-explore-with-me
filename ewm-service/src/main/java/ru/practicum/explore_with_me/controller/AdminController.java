package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.model.category.CategoryDto;
import ru.practicum.explore_with_me.model.compilation.CompilationDto;
import ru.practicum.explore_with_me.model.event.AdminUpdateEventDto;
import ru.practicum.explore_with_me.model.event.EventState;
import ru.practicum.explore_with_me.model.event.FullEventDto;
import ru.practicum.explore_with_me.model.user.ReturnUserDto;
import ru.practicum.explore_with_me.model.user.UserDto;
import ru.practicum.explore_with_me.service.category.CategoryService;
import ru.practicum.explore_with_me.service.compilation.CompilationService;
import ru.practicum.explore_with_me.service.event.EventService;
import ru.practicum.explore_with_me.service.user.UserService;
import ru.practicum.explore_with_me.validation.Validation;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;

    //region /Admin/Users Handlers

    @GetMapping("/users")
    public Collection<ReturnUserDto> getUsersById(@RequestParam(value = "ids") Set<Long> ids,
                                                  @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return userService.getUsersByIdWithPagination(ids, from, size);
    }

    @PostMapping("/users")
    @Validated(Validation.OnCreate.class)
    public ReturnUserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable(name = "userId") Long id) {
        userService.deleteUser(id);
    }

    //endregion

    //region /Admin/Categories Handlers

    @PostMapping("/categories")
    @Validated(Validation.OnCreate.class)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/categories")
    @Validated(Validation.OnPatch.class)
    public CategoryDto patchCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.patchCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

    //endregion

    //region /Admin/Compilations Handlers

    @PostMapping("/compilations")
    public CompilationDto createCompilation(@Valid @RequestBody CompilationDto compilationDto) {
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public CompilationDto deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        return compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public CompilationDto addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        return compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public CompilationDto unpinCompilation(@PathVariable Long compId) {
        return compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public CompilationDto pinCompilation(@PathVariable Long compId) {
        return compilationService.pinCompilation(compId);
    }

    //endregion

    //region /Admin/Events Handlers

    @GetMapping("/events")
    public Collection<FullEventDto> findEvents(@RequestParam(value = "users") Set<Long> userIds,
                                               @RequestParam(value = "states") Set<EventState> states,
                                               @RequestParam(value = "categories") Set<Long> catIds,
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                               @RequestParam(value = "rangeStart") LocalDateTime start,
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                               @RequestParam(value = "rangeEnd") LocalDateTime end,
                                               @RequestParam(value = "from", defaultValue = "0") Integer from,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return eventService.findEvents(userIds, states, catIds, start, end, from, size);
    }

    @PutMapping("/events/{eventId}")
    public FullEventDto updateEvent(@PathVariable Long eventId, @Valid @RequestBody AdminUpdateEventDto dto) {
        return eventService.adminUpdateEvent(eventId, dto);
    }

    @PatchMapping("/events/{eventId}/publish")
    public FullEventDto publishEvent(@PathVariable Long eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public FullEventDto rejectEvent(@PathVariable Long eventId) {
        return eventService.rejectEvent(eventId);
    }

    //endregion
}
