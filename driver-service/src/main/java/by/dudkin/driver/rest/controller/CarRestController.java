package by.dudkin.driver.rest.controller;

import by.dudkin.driver.rest.api.CarApi;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
import by.dudkin.driver.rest.dto.response.PaginatedResponse;
import by.dudkin.driver.service.api.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
public class CarRestController implements CarApi {

    private final CarService carService;

    @Override
    public ResponseEntity<CarResponse> get(long carId) {
        return new ResponseEntity<>(carService.findById(carId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PaginatedResponse<CarResponse>> getAll(Pageable pageable) {
        return new ResponseEntity<>(carService.findAll(pageable), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CarResponse> save(CarRequest carRequest) {
        return new ResponseEntity<>(carService.create(carRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CarResponse> update(long carId, CarRequest carRequest) {
        return new ResponseEntity<>(carService.update(carId, carRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(long carId) {
        carService.delete(carId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
