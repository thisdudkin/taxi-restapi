package by.dudkin.driver.rest.controller;

import by.dudkin.driver.aspect.TrackMetric;
import by.dudkin.driver.service.DriverLocationService;
import by.dudkin.driver.util.DriverLocation;
import by.dudkin.driver.util.MetricUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping(value = DriverLocationRestController.URI)
public class DriverLocationRestController {
    public static final String URI = "/api/drivers";
    private final DriverLocationService driverLocationService;

    @PutMapping(value = "/{driverId}/location")
    @TrackMetric(metricName = MetricUtils.DRIVERS_LOCATION_UPDATED_COUNT)
    public ResponseEntity<Void> updateCoordinates(@PathVariable UUID driverId,
                                                  @RequestBody DriverLocation location) {
        this.driverLocationService.update(driverId, location);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
