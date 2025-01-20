package by.dudkin.notification.aspect;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.notification.advice.DriverNotFoundException;
import by.dudkin.notification.dto.DriverResponse;
import by.dudkin.notification.feign.DriverClient;
import feign.FeignException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Aspect
@Component
public class NotificationAspect {

    private final DriverClient driverClient;

    public NotificationAspect(DriverClient driverClient) {
        this.driverClient = driverClient;
    }

    @Around("execution(* by.dudkin.notification.controller.NotificationController.getNearbyRequests(..)) && args(driverId, ..)")
    public Object checkDriverExists(ProceedingJoinPoint joinPoint, UUID driverId) throws Throwable {
        try {
            driverClient.getDriver(driverId);
        } catch (FeignException e) {
            throw new DriverNotFoundException(ErrorMessages.DRIVER_NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.GENERAL_ERROR);
        }
        return joinPoint.proceed();
    }

}
