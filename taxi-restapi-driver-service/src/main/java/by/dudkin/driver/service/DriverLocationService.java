package by.dudkin.driver.service;

import by.dudkin.driver.domain.DriverLocationDocument;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.util.DriverLocation;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Service
@AllArgsConstructor
public class DriverLocationService {

    private final DriverLocationRepository driverLocationRepository;

    public void update(UUID driverId, DriverLocation driverLocation) {
        GeoPoint location = new GeoPoint(driverLocation.lat().doubleValue(), driverLocation.lng().doubleValue());
        DriverLocationDocument locationDocument = new DriverLocationDocument(driverId, location);
        driverLocationRepository.save(locationDocument);
    }

}
