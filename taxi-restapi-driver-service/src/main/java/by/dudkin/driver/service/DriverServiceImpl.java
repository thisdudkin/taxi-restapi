package by.dudkin.driver.service;

import by.dudkin.common.enums.DriverStatus;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.kafka.producer.AvailableDriverProducer;
import by.dudkin.driver.mapper.DriverMapper;
import by.dudkin.driver.repository.DriverRepository;
import by.dudkin.driver.rest.advice.custom.DriverNotFoundException;
import by.dudkin.driver.rest.advice.custom.NoAvailableDriverException;
import by.dudkin.driver.rest.dto.request.AvailableDriver;
import by.dudkin.driver.rest.dto.request.DriverRequest;
import by.dudkin.driver.rest.dto.request.FeedbackRequest;
import by.dudkin.driver.rest.dto.response.DriverResponse;
import by.dudkin.driver.rest.dto.response.PendingRide;
import by.dudkin.driver.service.api.DriverService;
import by.dudkin.driver.util.DriverStatusTransition;
import by.dudkin.driver.util.DriverStatusTransitionValidator;
import by.dudkin.driver.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverMapper driverMapper;
    private final DriverRepository driverRepository;
    private final AvailableDriverService availableDriverService;
    private final AvailableDriverProducer availableDriverProducer;
    private final DriverStatusTransitionValidator transitionValidator;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<DriverResponse> findAll(Pageable pageable) {
        Page<Driver> driverPage = driverRepository.findAll(pageable);
        List<DriverResponse> driverResponses = driverPage.getContent().stream()
            .map(driverMapper::toResponse)
            .toList();

        return PaginatedResponse.fromPage(driverPage, driverResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public DriverResponse findById(UUID carId) {
        return driverMapper.toResponse(getOrThrow(carId));
    }

    @Override
    public DriverResponse search(String username) {
        return driverRepository.findByUsername(username)
            .map(driverMapper::toResponse)
            .orElseThrow(() -> new DriverNotFoundException(ErrorMessages.DRIVER_NOT_FOUND));
    }

    @Override
    public DriverResponse create(DriverRequest driverRequest) {
        Driver driver = driverMapper.toDriver(driverRequest);
        driver.setUsername(JwtTokenUtils.getPreferredUsername());
        return driverMapper.toResponse(driverRepository.save(driver));
    }

    @Override
    public DriverResponse update(UUID driverId, DriverRequest driverRequest) {
        Driver targetDriver = getOrThrow(driverId);
        driverMapper.updateDriver(driverRequest, targetDriver);
        driverRepository.save(targetDriver);
        return driverMapper.toResponse(targetDriver);
    }

    @Override
    public void delete(UUID driverId) {
        Driver driver = getOrThrow(driverId);
        driverRepository.delete(driver);
    }

    @Override
    public Driver getOrThrow(UUID driverId) {
        return driverRepository.findWithAssignmentsAndCarsById(driverId)
            .orElseThrow(() -> new DriverNotFoundException(ErrorMessages.DRIVER_NOT_FOUND));
    }

    @Override
    public void handleDriver(PendingRide ride) {
        try {
            AvailableDriver availableDriver = availableDriverService.findAvailableDriver(ride);
            availableDriverProducer.sendMessage(availableDriver);
        } catch (NoAvailableDriverException e) {
            log.warn(ErrorMessages.AVAILABLE_DRIVER_NOT_FOUND);
        }
    }

    @Override
    public DriverResponse markAvailable(UUID driverId) {
        Driver driver = getOrThrow(driverId);
        DriverStatusTransition transition = new DriverStatusTransition(driver.getStatus(), DriverStatus.READY);
        transitionValidator.validate(transition, new BeanPropertyBindingResult(transition, transition.getClass().getSimpleName()));
        driver.setStatus(DriverStatus.READY);
        return driverMapper.toResponse(driverRepository.save(driver));
    }

    @Override
    public DriverResponse markBusy(UUID driverId) {
        Driver driver = getOrThrow(driverId);
        DriverStatusTransition transition = new DriverStatusTransition(driver.getStatus(), DriverStatus.ON_TRIP);
        transitionValidator.validate(transition, new BeanPropertyBindingResult(transition, transition.getClass().getSimpleName()));
        driver.setStatus(DriverStatus.ON_TRIP);
        return driverMapper.toResponse(driverRepository.save(driver));
    }

    @Override
    public DriverResponse markOffline(UUID driverId) {
        Driver driver = getOrThrow(driverId);
        DriverStatusTransition transition = new DriverStatusTransition(driver.getStatus(), DriverStatus.OFFLINE);
        transitionValidator.validate(transition, new BeanPropertyBindingResult(transition, transition.getClass().getSimpleName()));
        driver.setStatus(DriverStatus.OFFLINE);
        return driverMapper.toResponse(driverRepository.save(driver));
    }

    @Override
    public DriverResponse rateDriver(UUID driverId, FeedbackRequest feedbackRequest) {
        driverRepository.rateDriver(driverId, feedbackRequest.rating());
        return driverMapper.toResponse(getOrThrow(driverId));
    }

    @Override
    public void updateBalance(UUID driverId, BigDecimal amount) {
        driverRepository.updateBalance(driverId, amount);
    }

}
