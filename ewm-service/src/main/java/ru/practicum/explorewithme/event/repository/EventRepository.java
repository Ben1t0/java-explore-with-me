package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(long id, Pageable page);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :catIds) AND " +
            "(LOWER(e.annotation) LIKE CONCAT('%',LOWER(:text),'%') OR " +
            "LOWER(e.description) LIKE CONCAT('%',LOWER(:text),'%')) AND " +
            "e.eventDate > :now AND " +
            "e.state = 'PUBLISHED' AND " +
            "e.paid = :paid")
    Collection<Event> findAfterDate(String text, Collection<Long> catIds, boolean paid, LocalDateTime now);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :catIds) AND " +
            "(LOWER(e.annotation) LIKE CONCAT('%',LOWER(:text),'%') OR " +
            "LOWER(e.description) LIKE CONCAT('%',LOWER(:text),'%')) AND " +
            "(e.eventDate >= :start AND e.eventDate <= :end) AND " +
            "e.state = 'PUBLISHED' AND " +
            "e.paid = :paid")
    Collection<Event> findBetweenDates(String text, Collection<Long> catIds, boolean paid, LocalDateTime start,
                                       LocalDateTime end);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :catIds) AND " +
            "(e.initiator.id IN :userIds) AND " +
            "(e.state IN :states) AND " +
            "(e.eventDate >= :start AND e.eventDate <= :end)")
    List<Event> findBetweenDatesByUsersStatesCategories(Collection<Long> userIds, Collection<EventState> states,
                                                              Collection<Long> catIds, LocalDateTime start,
                                                              LocalDateTime end, Pageable pageable);
}