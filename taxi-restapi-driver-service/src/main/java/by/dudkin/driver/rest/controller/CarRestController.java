package by.dudkin.driver.rest.controller;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.aspect.TrackMetric;
import by.dudkin.driver.rest.api.CarApi;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
import by.dudkin.driver.service.api.CarService;
import by.dudkin.driver.util.MetricUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
public class CarRestController implements CarApi {

    private final CarService carService;

    @Override
    public ResponseEntity<CarResponse> get(UUID carId) {
        return new ResponseEntity<>(carService.findById(carId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PaginatedResponse<CarResponse>> getAll(Pageable pageable) {
        return new ResponseEntity<>(carService.findAll(pageable), HttpStatus.OK);
    }

    @Override
    @TrackMetric(metricName = MetricUtils.CARS_CREATED_COUNT)
    public ResponseEntity<CarResponse> save(CarRequest carRequest) {
        return new ResponseEntity<>(carService.create(carRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CarResponse> update(UUID carId, CarRequest carRequest) {
        return new ResponseEntity<>(carService.update(carId, carRequest), HttpStatus.OK);
    }

    @Override
    @TrackMetric(metricName = MetricUtils.CARS_DELETED_COUNT)
    public ResponseEntity<Void> delete(UUID carId) {
        carService.delete(carId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
