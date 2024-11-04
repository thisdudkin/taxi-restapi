package by.dudkin.driver.service;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.domain.Car;
import by.dudkin.driver.mapper.CarMapper;
import by.dudkin.driver.repository.CarRepository;
import by.dudkin.driver.rest.advice.CarNotFoundException;
import by.dudkin.driver.rest.advice.DuplicateLicensePlateException;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
import by.dudkin.driver.service.api.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarMapper carMapper;
    private final CarRepository carRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CarResponse> findAll(Pageable pageable) {
        return carRepository.findAll(pageable).map(carMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CarResponse findById(long carId) {
        return carMapper.toResponse(getOrThrow(carId));
    }

    @Override
    public CarResponse create(CarRequest carRequest) {
        if (carRepository.existsByLicensePlate(carRequest.licensePlate()))
            throw new DuplicateLicensePlateException(ErrorMessages.DUPLICATE_LICENSE_PLATE);
        return carMapper.toResponse(carRepository.save(carMapper.toCar(carRequest)));
    }

    @Override
    public CarResponse update(long carId, CarRequest carRequest) {
        Car targetCar = getOrThrow(carId);
        carMapper.updateCar(carRequest, targetCar);
        carRepository.save(targetCar);
        return carMapper.toResponse(targetCar);
    }

    @Override
    public void delete(long carId) {
        Car car = getOrThrow(carId);
        carRepository.delete(car);
    }

    private Car getOrThrow(long carId) {
        return carRepository.findWithAssignmentsAndDriversById(carId)
                .orElseThrow(() -> new CarNotFoundException(ErrorMessages.CAR_NOT_FOUND));
    }

}
