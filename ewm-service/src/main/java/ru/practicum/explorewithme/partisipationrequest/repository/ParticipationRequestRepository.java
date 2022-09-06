package ru.practicum.explorewithme.partisipationrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.partisipationrequest.model.ParticipationRequest;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
}
