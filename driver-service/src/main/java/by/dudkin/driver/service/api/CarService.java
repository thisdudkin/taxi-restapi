package by.dudkin.driver.service.api;

import by.dudkin.driver.domain.Car;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface CarService {

    Page<CarResponse> findAll(Pageable pageable);

    CarResponse findById(long carId);

    CarResponse create(CarRequest carRequest);

    CarResponse update(long carId, CarRequest carRequest);

    void delete(long carId);

    Car getOrThrow(long carId);

}
