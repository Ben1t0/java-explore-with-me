package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.model.event.CreateEventDto;
import ru.practicum.explore_with_me.model.event.FullEventDto;
import ru.practicum.explore_with_me.model.event.ShortEventDto;
import ru.practicum.explore_with_me.model.request.CreateRequestDto;
import ru.practicum.explore_with_me.model.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.service.event.EventService;
import ru.practicum.explore_with_me.service.request.ParticipationRequestService;
import ru.practicum.explore_with_me.validation.Validation;

import javax.validation.Valid;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final EventService eventService;
    private final ParticipationRequestService requestService;

    @GetMapping("/{userId}/events")
    public Collection<ShortEventDto> getUserEvents(@PathVariable Long userId,
                                                   @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @Validated(Validation.OnCreate.class)
    public FullEventDto createEvent(@PathVariable Long userId, @Valid @RequestBody CreateEventDto createEventDto) {
        return eventService.createEvent(userId, createEventDto);
    }

    @PatchMapping("/{userId}/events")
    @Validated(Validation.OnPatch.class)
    public FullEventDto patchEvent(@PathVariable Long userId, @Valid @RequestBody CreateEventDto createEventDto) {
        return eventService.patchEvent(userId, createEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public FullEventDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public FullEventDto cancelUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.cancelUserEvent(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getRequestsForUserEvents(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getRequestsForUserEvents(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto approveRequest(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long reqId) {
        return requestService.approveRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId) {
        return requestService.rejectRequest(userId, eventId, reqId);
    }

    @GetMapping("/{userId}/requests")
    public Collection<ParticipationRequestDto> getUserEvents(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @Validated(Validation.OnCreate.class)
    public ParticipationRequestDto createEvent(@PathVariable Long userId,
                                               @Valid @RequestBody CreateRequestDto createRequestDto) {
        return requestService.createRequest(userId, createRequestDto);
    }

    @PatchMapping("/{userId}/requests/{reqId}/cancel")
    public ParticipationRequestDto patchEvent(@PathVariable Long userId, @PathVariable Long reqId) {
        return requestService.cancelRequest(userId, reqId);
    }
}
