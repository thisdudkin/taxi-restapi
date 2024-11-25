package by.dudkin.driver.util;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.repository.CarRepository;
import by.dudkin.driver.rest.advice.custom.DuplicateLicensePlateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alexander Dudkin
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarValidator {

    private final CarRepository carRepository;

    public void validateUniqueLicensePlate(String licensePlate) {
        if (carRepository.existsByLicensePlate(licensePlate)) {
            throw new DuplicateLicensePlateException(ErrorMessages.DUPLICATE_LICENSE_PLATE);
        }
    }

}
