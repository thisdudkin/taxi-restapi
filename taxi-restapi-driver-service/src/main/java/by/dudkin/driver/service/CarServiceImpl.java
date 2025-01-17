package by.dudkin.driver.service;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.domain.Car;
import by.dudkin.driver.mapper.CarMapper;
import by.dudkin.driver.repository.CarRepository;
import by.dudkin.driver.rest.advice.custom.CarNotFoundException;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.service.api.CarService;
import by.dudkin.driver.util.CarValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarMapper carMapper;
    private final CarRepository carRepository;
    private final CarValidator carValidator;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<CarResponse> findAll(Pageable pageable) {
        Page<Car> carPage = carRepository.findAll(pageable);
        List<CarResponse> carResponses = carPage.getContent().stream()
                .map(carMapper::toResponse)
                .toList();

        return PaginatedResponse.fromPage(carPage, carResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public CarResponse findById(UUID carId) {
        return carMapper.toResponse(getOrThrow(carId));
    }

    @Override
    public CarResponse create(CarRequest carRequest) {
        carValidator.validateUniqueLicensePlate(carRequest.licensePlate());
        return carMapper.toResponse(carRepository.save(carMapper.toCar(carRequest)));
    }

    @Override
    public CarResponse update(UUID carId, CarRequest carRequest) {
        Car targetCar = getOrThrow(carId);
        carMapper.updateCar(carRequest, targetCar);
        carRepository.save(targetCar);
        return carMapper.toResponse(targetCar);
    }

    @Override
    public void delete(UUID carId) {
        Car car = getOrThrow(carId);
        carRepository.delete(car);
    }

    @Override
    public Car getOrThrow(UUID carId) {
        return carRepository.findWithAssignmentsAndDriversById(carId)
                .orElseThrow(() -> new CarNotFoundException(ErrorMessages.CAR_NOT_FOUND));
    }

    @Override
    public Car getOrThrow(String licensePlate) {
        return carRepository.findByLicensePlate(licensePlate)
            .orElseThrow(() -> new CarNotFoundException(ErrorMessages.CAR_NOT_FOUND));
    }

}
