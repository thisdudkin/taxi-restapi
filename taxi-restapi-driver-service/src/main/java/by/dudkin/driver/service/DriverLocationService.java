package by.dudkin.driver.service;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.domain.DriverLocationDocument;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.repository.DriverRepository;
import by.dudkin.driver.rest.advice.custom.DriverNotFoundException;
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
    private final DriverRepository driverRepository;

    public void update(String username, DriverLocation driverLocation) {
        driverRepository.findByUsername(username)
            .map(Driver::getId)
            .map(driverId -> {
                GeoPoint location = new GeoPoint(
                    driverLocation.lat().doubleValue(),
                    driverLocation.lng().doubleValue()
                );
                return new DriverLocationDocument(driverId, location);
            })
            .ifPresentOrElse(
                driverLocationRepository::save,
                () -> {
                    throw new DriverNotFoundException(ErrorMessages.DRIVER_NOT_FOUND);
                }
            );
    }

}
