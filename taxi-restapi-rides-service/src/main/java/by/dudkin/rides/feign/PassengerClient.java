package by.dudkin.rides.feign;

import by.dudkin.rides.configuration.OpenFeignBearerTokenInterceptorConfiguration;
import by.dudkin.rides.rest.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "passenger-service",
    path = "/api/passengers",
    configuration = OpenFeignBearerTokenInterceptorConfiguration.class
)
public interface PassengerClient {

    @GetMapping("/search")
    PassengerResponse getPassengerByUsername(@RequestParam String username);

}
