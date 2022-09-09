package ru.practicum.explorewithme.partisipationrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.partisipationrequest.model.ParticipationRequest;

import java.util.Collection;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Collection<ParticipationRequest> findAllByEventId(Long eventId);

    Collection<ParticipationRequest> findAllByRequesterId(Long id);
}
