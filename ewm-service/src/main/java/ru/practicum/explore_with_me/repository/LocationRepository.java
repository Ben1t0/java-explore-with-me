package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.model.location.Location;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location as l " +
            "WHERE function('distance',:latitude,:longitude,l.latitude,l.longitude) <= l.radius " +
            "ORDER BY function('distance',:latitude,:longitude,l.latitude,l.longitude)")
    List<Location> getNearestLocations(float latitude, float longitude, Pageable pageable);
}
