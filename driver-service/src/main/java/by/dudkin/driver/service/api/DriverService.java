package by.dudkin.driver.service.api;

import by.dudkin.driver.rest.dto.request.DriverRequest;
import by.dudkin.driver.rest.dto.response.DriverResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface DriverService {

    Page<DriverResponse> findAll(Pageable pageable);

    DriverResponse findById(long carId);

    DriverResponse create(DriverRequest driverRequest);

    DriverResponse update(long driverId, DriverRequest driverRequest);

    void delete(long driverId);

}
