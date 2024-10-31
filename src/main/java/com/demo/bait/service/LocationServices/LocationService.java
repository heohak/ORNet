package com.demo.bait.service.LocationServices;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.LocationMapper;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.service.CommentServices.CommentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class LocationService {

    private LocationRepo locationRepo;
    private LocationMapper locationMapper;
    private EntityManager entityManager;
    private LocationMaintenanceService locationMaintenanceService;
    private CommentService commentService;

    @Transactional
    public LocationDTO addLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.name());
//        location.setAddress(locationDTO.address());
        location.setCountry(locationDTO.country());
        location.setCity(locationDTO.city());
        location.setStreetAddress(locationDTO.streetAddress());
        location.setPostalCode(locationDTO.postalCode());
        location.setPhone(locationDTO.phone());
        location.setEmail(locationDTO.email());
        location.setLastMaintenance(locationDTO.lastMaintenance());
        location.setNextMaintenance(locationDTO.nextMaintenance());
        locationMaintenanceService.updateLocationMaintenance(location, locationDTO);
        if (locationDTO.commentIds() != null) {
            Set<Comment> comments = commentService.commentIdsToCommentsSet(locationDTO.commentIds());
            location.setComments(comments);
        }
        locationRepo.save(location);
        return locationMapper.toDto(location);
    }

    @Transactional
    public ResponseDTO deleteLocation(Integer locationId) {
        locationRepo.deleteById(locationId);
        return new ResponseDTO("Location deleted successfully");
    }

    @Transactional
    public ResponseDTO updateLocation(Integer locationId, LocationDTO locationDTO) {
        Optional<Location> locationOpt = locationRepo.findById(locationId);
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + " not found");
        }
        Location location = locationOpt.get();

        updateName(location, locationDTO);
//        updateAddress(location, locationDTO);
        updateCountry(location, locationDTO);
        updateCity(location, locationDTO);
        updateStreetAddress(location, locationDTO);
        updatePostalCode(location, locationDTO);
        updatePhone(location, locationDTO);
        updateEmail(location, locationDTO);
        updateLastMaintenance(location, locationDTO);
        updateNextMaintenance(location, locationDTO);
        locationMaintenanceService.updateLocationMaintenance(location, locationDTO);
        locationRepo.save(location);
        return new ResponseDTO("Location updated successfully");
    }

    public void updateName(Location location, LocationDTO locationDTO) {
        if (locationDTO.name() != null) {
            location.setName(locationDTO.name());
        }
    }

//    public void updateAddress(Location location, LocationDTO locationDTO) {
//        if (locationDTO.address() != null) {
//            location.setAddress(locationDTO.address());
//        }
//    }

    public void updateCountry(Location location, LocationDTO locationDTO) {
        if (locationDTO.country() != null) {
            location.setCountry(locationDTO.country());
        }
    }

    public void updateCity(Location location, LocationDTO locationDTO) {
        if (locationDTO.city() != null) {
            location.setCity(locationDTO.city());
        }
    }

    public void updateStreetAddress(Location location, LocationDTO locationDTO) {
        if (locationDTO.streetAddress() != null) {
            location.setStreetAddress(locationDTO.streetAddress());
        }
    }

    public void updatePostalCode(Location location, LocationDTO locationDTO) {
        if (locationDTO.postalCode() != null) {
            location.setPostalCode(locationDTO.postalCode());
        }
    }

    public void updatePhone(Location location, LocationDTO locationDTO) {
        if (locationDTO.phone() != null) {
            location.setPhone(locationDTO.phone());
        }
    }

    public void updateEmail(Location location, LocationDTO locationDTO) {
        if (locationDTO.email() != null) {
            location.setEmail(locationDTO.email());
        }
    }

    public void updateLastMaintenance(Location location, LocationDTO locationDTO) {
        if (locationDTO.lastMaintenance() != null) {
            location.setLastMaintenance(locationDTO.lastMaintenance());
        }
    }

    public void updateNextMaintenance(Location location, LocationDTO locationDTO) {
        if (locationDTO.nextMaintenance() != null) {
            location.setNextMaintenance(locationDTO.nextMaintenance());
        }
    }

    public LocationDTO getLocationById(Integer locationId) {
        Optional<Location> locationOpt = locationRepo.findById(locationId);
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + " not found");
        }
        return locationMapper.toDto(locationOpt.get());
    }

    public List<LocationDTO> getAllLocations() {
        List<LocationDTO> locations = locationMapper.toDtoList(locationRepo.findAll());
        locations.sort(Comparator.comparing(LocationDTO::name, String::compareToIgnoreCase));
        return locations;
    }

    public Set<Location> locationIdsToLocationsSet(List<Integer> locationIds) {
        Set<Location> locations = new HashSet<>();
        for (Integer locationId : locationIds) {
            Location location = locationRepo.findById(locationId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid location ID: " + locationId));
            locations.add(location);
        }
        return locations;
    }

    public List<LocationDTO> getLocationHistory(Integer locationId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(Location.class, locationId);

        List<Location> history = new ArrayList<>();
        for (Number rev : revisions) {
            Location LocationVersion = auditReader
                    .find(Location.class, locationId, rev);
            history.add(LocationVersion);
        }
        return locationMapper.toDtoList(history);
    }

    public List<String> getAllLocationCountries() {
        List<Location> locations = locationRepo.findAll();

        return locations.stream()
                .map(Location::getCountry)
                .filter(country -> country != null && !country.isEmpty())
                .map(country -> capitalize(country.toLowerCase()))
                .collect(Collectors.toSet())
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private String capitalize(String country) {
        return Character.toUpperCase(country.charAt(0)) + country.substring(1);
    }
}
