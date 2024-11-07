package by.dudkin.driver.service.api;

import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.rest.dto.request.DriverRequest;
import by.dudkin.driver.rest.dto.response.DriverResponse;
import by.dudkin.driver.rest.dto.response.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface DriverService {

    PaginatedResponse<DriverResponse> findAll(Pageable pageable);

    DriverResponse findById(long carId);

    DriverResponse create(DriverRequest driverRequest);

    DriverResponse update(long driverId, DriverRequest driverRequest);

    void delete(long driverId);

    Driver getOrThrow(long driverId);

}
