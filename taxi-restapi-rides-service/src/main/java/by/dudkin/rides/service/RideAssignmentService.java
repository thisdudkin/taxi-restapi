package by.dudkin.rides.service;

import by.dudkin.common.enums.DriverStatus;
import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.feign.DriverClient;
import by.dudkin.rides.rest.advice.custom.EntityValidationConflictException;
import by.dudkin.rides.rest.dto.response.AvailableDriver;
import by.dudkin.rides.rest.dto.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RideAssignmentService {

    private final DriverClient driverClient;

    public Ride assignDriverToRide(Ride ride, AvailableDriver availableDriver, DriverResponse driver) {
        validateStatus(ride, driver);
        ride.setDriverId(availableDriver.driverId());
        ride.setCarId(availableDriver.carId());
        ride.setStatus(RideStatus.ASSIGNED);

        driverClient.markDriverBusy(driver.id());

        return ride;
    }

    private void validateStatus(Ride ride, DriverResponse driver) {
        if (driver.status() != DriverStatus.READY) {
            throw new EntityValidationConflictException(ErrorMessages.DRIVER_IS_NOT_READY);
        }

        if (ride.getStatus() != RideStatus.PENDING) {
            throw new EntityValidationConflictException(ErrorMessages.RIDE_IS_NOT_PENDING);
        }
    }

}
