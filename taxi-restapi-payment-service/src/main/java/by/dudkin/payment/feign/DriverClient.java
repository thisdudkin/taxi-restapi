package by.dudkin.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "drivers-service",
    path = "/api/drivers"
)
public interface DriverClient {

    @PutMapping("/{driverId}/balance")
    void updateBalance(@PathVariable long driverId, @RequestParam BigDecimal amount);

}
