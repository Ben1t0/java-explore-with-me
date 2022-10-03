package ru.practicum.explore_with_me.service.location;

import ru.practicum.explore_with_me.model.location.Location;
import ru.practicum.explore_with_me.model.location.LocationDto;

import java.util.List;

public interface LocationService {

    List<LocationDto> getAllLocations(Integer from, Integer size, Float longitude, Float latitude);

    LocationDto createLocation(LocationDto location);

    void deleteLocation(long id);

    LocationDto patchLocation(LocationDto location);

    Location getLocation(long id);
}
