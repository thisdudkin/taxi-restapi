package by.dudkin.notification.feign;

import by.dudkin.notification.config.OpenFeignBearerTokenInterceptorConfiguration;
import by.dudkin.notification.dto.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "driver-service",
    path = "/api/drivers",
    configuration = OpenFeignBearerTokenInterceptorConfiguration.class
)
public interface DriverClient {

    @GetMapping("/search")
    ResponseEntity<DriverResponse> search(@RequestParam String username);

}
