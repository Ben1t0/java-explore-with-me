package ru.practicum.explore_with_me.model.location;

public class LocationMapper {
    public static LocationDto toDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .radius(location.getRadius())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build();
    }

    public static Location fromDto(LocationDto locationDto) {
        return Location.builder()
                .name(locationDto.getName())
                .radius(locationDto.getRadius())
                .longitude(locationDto.getLongitude())
                .latitude(locationDto.getLatitude())
                .build();
    }
}
