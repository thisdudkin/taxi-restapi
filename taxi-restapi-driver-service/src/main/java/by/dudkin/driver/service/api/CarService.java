package by.dudkin.driver.service.api;

import by.dudkin.driver.domain.Car;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
import by.dudkin.common.util.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface CarService {

    PaginatedResponse<CarResponse> findAll(Pageable pageable);
    CarResponse findById(UUID carId);
    CarResponse create(CarRequest carRequest);
    CarResponse update(UUID carId, CarRequest carRequest);
    void delete(UUID carId);

    Car getOrThrow(UUID carId);
    Car getOrThrow(String licensePlate);

}
