package by.dudkin.driver.util;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.repository.CarRepository;
import by.dudkin.driver.rest.advice.DuplicateLicensePlateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Dudkin
 */
@Service
@RequiredArgsConstructor
public class CarValidator {

    private final CarRepository carRepository;

    public void validateUniqueLicensePlate(String licensePlate) {
        if (carRepository.existsByLicensePlate(licensePlate)) {
            throw new DuplicateLicensePlateException(ErrorMessages.DUPLICATE_LICENSE_PLATE);
        }
    }

}
