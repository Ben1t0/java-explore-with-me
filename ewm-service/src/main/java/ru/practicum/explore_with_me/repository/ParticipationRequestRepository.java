package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.model.request.ParticipationRequest;

import java.util.Collection;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Collection<ParticipationRequest> findAllByEventId(Long eventId);

    Collection<ParticipationRequest> findAllByRequesterId(Long id);
}
