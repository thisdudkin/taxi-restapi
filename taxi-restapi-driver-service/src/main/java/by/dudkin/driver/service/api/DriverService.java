package by.dudkin.driver.service.api;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.rest.dto.request.DriverRequest;
import by.dudkin.driver.rest.dto.response.DriverResponse;
import by.dudkin.driver.rest.dto.response.PendingRide;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface DriverService {

    PaginatedResponse<DriverResponse> findAll(Pageable pageable);
    DriverResponse findById(UUID driverId);
    DriverResponse search(String username);
    DriverResponse create(DriverRequest driverRequest);
    DriverResponse update(UUID driverId, DriverRequest driverRequest);
    void delete(UUID driverId);

    Driver getOrThrow(UUID driverId);

    void handleDriver(PendingRide ride);

    DriverResponse markAvailable(UUID driverId);
    DriverResponse markBusy(UUID driverId);
    DriverResponse markOffline(UUID driverId);

    void updateBalance(UUID driverId, BigDecimal amount);

}
