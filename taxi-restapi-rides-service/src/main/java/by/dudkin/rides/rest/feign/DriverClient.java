package by.dudkin.rides.rest.feign;

import by.dudkin.rides.rest.dto.response.CarResponse;
import by.dudkin.rides.rest.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "drivers-service",
    path = "/api/"
)
public interface DriverClient {

    @GetMapping("/drivers/{driverId}")
    DriverResponse getDriverById(@PathVariable UUID driverId);

    @PutMapping("/drivers/{driverId}/status/busy")
    void markDriverBusy(@PathVariable UUID driverId);

    @PutMapping("/drivers/{driverId}/status/available")
    void markDriverAvailable(@PathVariable UUID driverId);

    @GetMapping("/cars/{carId}")
    CarResponse getCarById(@PathVariable UUID carId);

}
