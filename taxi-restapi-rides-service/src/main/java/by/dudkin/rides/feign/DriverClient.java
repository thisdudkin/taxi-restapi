package by.dudkin.rides.feign;

import by.dudkin.rides.configuration.OpenFeignBearerTokenInterceptorConfiguration;
import by.dudkin.rides.rest.dto.response.AvailableDriver;
import by.dudkin.rides.rest.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "drivers-service",
    path = "/api/",
    configuration = OpenFeignBearerTokenInterceptorConfiguration.class
)
public interface DriverClient {

    @GetMapping("/drivers/{driverId}")
    DriverResponse getDriverById(@PathVariable UUID driverId);

    @GetMapping("/drivers/search")
    DriverResponse getDriverByUsername(@RequestParam String username);

    @GetMapping("/assignments/search")
    AvailableDriver getAssignmentByUsername(@RequestParam String username);

    @PutMapping("/drivers/{driverId}/status/busy")
    void markDriverBusy(@PathVariable UUID driverId);

    @PutMapping("/drivers/{driverId}/status/available")
    void markDriverAvailable(@PathVariable UUID driverId);

}
