package ru.practicum.explore_with_me.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.exception.LocationBadRequestData;
import ru.practicum.explore_with_me.exception.LocationNotFoundException;
import ru.practicum.explore_with_me.model.location.Location;
import ru.practicum.explore_with_me.model.location.LocationDto;
import ru.practicum.explore_with_me.model.location.LocationMapper;
import ru.practicum.explore_with_me.repository.LocationRepository;
import ru.practicum.explore_with_me.utils.OffsetBasedPageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repository;

    @Override
    public List<LocationDto> getAllLocations(Integer from, Integer size, Float longitude, Float latitude) {
        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("name"));
        if (longitude != null && latitude != null) {
            if (longitude > 180 || longitude < -180) {
                throw new LocationBadRequestData("Longitude must be in range [-180; 180]");
            }
            if (latitude > 90 || latitude < -90) {
                throw new LocationBadRequestData("Latitude must be in range [-90; 90]");
            }

            return repository.getNearestLocations(latitude, longitude, page).stream()
                    .map(LocationMapper::toDto)
                    .collect(Collectors.toList());
        }
        return repository.findAll(page).stream()
                .map(LocationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LocationDto createLocation(LocationDto locationDto) {
        return LocationMapper.toDto(repository.save(LocationMapper.fromDto(locationDto)));
    }

    @Override
    public LocationDto patchLocation(LocationDto locationDto) {
        Location location = getLocation(locationDto.getId()).toBuilder().build();
        if (locationDto.getName() != null) {
            location.setName(locationDto.getName());
        }
        if (locationDto.getRadius() != null) {
            location.setRadius(locationDto.getRadius());
        }
        if (locationDto.getLongitude() != null) {
            location.setLongitude(locationDto.getLongitude());
        }
        if (locationDto.getLatitude() != null) {
            location.setLatitude(locationDto.getLatitude());
        }
        return LocationMapper.toDto(repository.save(location));
    }

    @Override
    public void deleteLocation(long id) {
        repository.deleteById(id);
    }

    @Override
    public Location getLocation(long id) {
        return repository.findById(id).orElseThrow(() -> new LocationNotFoundException(id));
    }
}
