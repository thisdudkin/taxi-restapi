package by.dudkin.rides.feign;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.rides.configuration.OpenFeignBearerTokenInterceptorConfiguration;
import by.dudkin.rides.rest.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "passengers-service",
    path = "/api/passengers",
    configuration = OpenFeignBearerTokenInterceptorConfiguration.class
)
public interface PassengerClient {

    @GetMapping("/{id}")
    PassengerResponse getPassengerById(@PathVariable UUID id);

    @GetMapping("/search")
    PassengerResponse getPassengerByUsername(@RequestParam String username);

}
