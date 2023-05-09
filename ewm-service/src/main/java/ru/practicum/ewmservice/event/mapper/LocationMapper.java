package ru.practicum.ewmservice.event.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.event.DTO.LocationDTO;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.event.repository.LocationRepository;

@Component
public class LocationMapper {

    private final LocationRepository locationRepository;

    public LocationMapper(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location mapDTOToEntity(LocationDTO locationDTO) {
        Location location = Location.builder()
                .lon(locationDTO.getLon())
                .lat(locationDTO.getLat())
                .build();
        location = locationRepository.findByLatAndLon(location.getLat(), location.getLon())
                .orElse(locationRepository.save(location));
        return location;
    }

    public LocationDTO mapEntityToDTO(Location location) {
        LocationDTO locationDTO = LocationDTO.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
        return locationDTO;
    }
}
