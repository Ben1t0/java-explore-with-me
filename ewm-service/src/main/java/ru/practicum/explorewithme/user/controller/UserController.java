package ru.practicum.explorewithme.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.CreateEventDto;
import ru.practicum.explorewithme.event.dto.FullEventDto;
import ru.practicum.explorewithme.event.dto.ShortEventDto;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.partisipationrequest.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.partisipationrequest.service.ParticipationRequestService;
import ru.practicum.explorewithme.validation.Validation;

import javax.validation.Valid;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
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
    public Collection<ParticipationRequestDto> getUserRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getUserRequests(userId, eventId);
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
}
