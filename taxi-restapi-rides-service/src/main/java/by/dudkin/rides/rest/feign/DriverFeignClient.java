package by.dudkin.rides.rest.feign;

import by.dudkin.rides.rest.dto.response.CarResponse;
import by.dudkin.rides.rest.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "drivers-service",
    path = "/api/"
)
public interface DriverFeignClient {

    @GetMapping("/drivers/{id}")
    DriverResponse getDriverById(@PathVariable Long id);

    @GetMapping("/cars/{id}")
    CarResponse getCarById(@PathVariable Long id);

}
