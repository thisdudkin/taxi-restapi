package by.dudkin.rides.feign;

import by.dudkin.rides.configuration.OpenFeignBearerTokenInterceptorConfiguration;
import by.dudkin.rides.rest.dto.response.Promocode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "promocode-service",
    path = "/api/promocodes",
    configuration = OpenFeignBearerTokenInterceptorConfiguration.class
)
public interface PromocodeClient {

    @GetMapping
    Promocode validate(@RequestParam String code);

}
