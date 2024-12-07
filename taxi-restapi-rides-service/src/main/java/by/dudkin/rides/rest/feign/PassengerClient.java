package by.dudkin.rides.rest.feign;

import by.dudkin.rides.rest.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "passengers-service",
    path = "/api/passengers"
)
public interface PassengerClient {

    @GetMapping("/{id}")
    PassengerResponse getPassengerById(@PathVariable Long id);

}
