package by.dudkin.notification.feign;

import by.dudkin.notification.config.OpenFeignBearerTokenInterceptorConfiguration;
import by.dudkin.notification.dto.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "drivers-service",
    path = "/api/drivers",
    configuration = OpenFeignBearerTokenInterceptorConfiguration.class
)
public interface DriverClient {

    @GetMapping("/{driverId}")
    ResponseEntity<DriverResponse> getDriver(@PathVariable UUID driverId);

}
