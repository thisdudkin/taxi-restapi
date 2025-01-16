package by.dudkin.rides.service;

import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.kafka.domain.AcceptedRideEvent;
import by.dudkin.rides.mapper.RideMapper;
import by.dudkin.rides.repository.RideRepository;
import by.dudkin.rides.rest.advice.custom.RideNotFoundException;
import by.dudkin.rides.rest.dto.request.PendingRide;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideCostRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.AvailableDriver;
import by.dudkin.rides.rest.dto.response.DriverResponse;
import by.dudkin.rides.rest.dto.response.RideCostResponse;
import by.dudkin.rides.rest.dto.response.RideResponse;
import by.dudkin.rides.feign.DriverClient;
import by.dudkin.rides.service.api.RideService;
import by.dudkin.rides.utils.GeospatialUtils;
import by.dudkin.rides.utils.PriceCalculator;
import by.dudkin.rides.utils.RideStatusTransition;
import by.dudkin.rides.utils.RideStatusTransitionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideMapper rideMapper;
    private final DriverClient driverClient;
    private final RideRepository rideRepository;
    private final RideCreationService rideCreationService;
    private final ApplicationEventPublisher eventPublisher;
    private final RideAssignmentService rideAssignmentService;
    private final RideCompletionService rideCompletionService;
    private final RideStatusTransitionValidator transitionValidator;
    private final PriceCalculator priceCalculator;

    @Override
    public RideResponse create(RideRequest req, String username) {
        Ride ride = rideMapper.toRide(req);
        Ride saved = rideRepository.save(rideCreationService.createRide(ride, req.promocode(), username));
        eventPublisher.publishEvent(new PendingRide(saved.getId(), saved.getFrom(), saved.getTo(), saved.getPrice()));
        return rideMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RideResponse read(UUID rideId) {
        return rideMapper.toResponse(getOrThrow(rideId));
    }

    @Override
    public RideResponse update(UUID rideId, RideRequest rideRequest) {
        Ride rideForUpdate = getOrThrow(rideId);
        rideMapper.updateRide(rideRequest, rideForUpdate);
        rideRepository.save(rideForUpdate);
        return rideMapper.toResponse(rideForUpdate);
    }

    @Override
    public void delete(UUID rideId) {
        rideRepository.delete(getOrThrow(rideId));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<RideResponse> readAll(Specification<Ride> spec, Pageable pageable) {
        Page<Ride> ridePage = rideRepository.findAll(spec, pageable);
        List<RideResponse> rideResponses = ridePage.getContent().stream()
                .map(rideMapper::toResponse)
                .toList();

        return PaginatedResponse.fromPage(ridePage, rideResponses);
    }

    @Override
    public RideResponse cancel(UUID rideId) {
        return rideMapper.toResponse(updateRideStatus(rideId, RideStatus.CANCEL));
    }

    @Override
    public RideResponse complete(UUID rideId) {
        Ride ride = updateRideStatus(rideId, RideStatus.DONE);
        return rideMapper.toResponse(rideRepository.save(rideCompletionService.completeRide(ride)));
    }

    @Override
    public RideResponse activate(UUID rideId) {
        Ride ride = updateRideStatus(rideId, RideStatus.ACTIVE);
        ride.setStartTime(LocalDateTime.now());
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    @Override
    public RideResponse rate(UUID rideId, RideCompletionRequest request) {
        Ride ride = getOrThrow(rideId);
        ride.setRating(request.rating());
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    @Override
    public RideResponse assign(UUID rideId, AvailableDriver availableDriver) {
        Ride ride = getOrThrow(rideId);
        RideStatusTransition transition = new RideStatusTransition(ride.getStatus(), RideStatus.ASSIGNED);
        transitionValidator.validate(transition, new BeanPropertyBindingResult(transition, transition.getClass().getSimpleName()));
        DriverResponse driver = driverClient.getDriverById(availableDriver.driverId());
        ride = rideAssignmentService.assignDriverToRide(ride, availableDriver, driver);
        Ride saved = rideRepository.save(ride);
        eventPublisher.publishEvent(new AcceptedRideEvent(saved.getId(), saved.getDriverId(), saved.getCarId()));
        return rideMapper.toResponse(saved);
    }

    @Override
    public RideCostResponse checkCost(RideCostRequest request) {
        double distance = GeospatialUtils.calculateDistance(
            request.from().getLat(), request.from().getLng(),
            request.to().getLat(), request.to().getLng()
        );
        double estimatedTime = GeospatialUtils.calculateEstimatedTime(distance);
        BigDecimal price = priceCalculator.calculatePrice(distance);

        return new RideCostResponse(price, estimatedTime, request.from(), request.to());
    }

    Ride getOrThrow(UUID rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(ErrorMessages.RIDE_NOT_FOUND));
    }

    private Ride updateRideStatus(UUID rideId, RideStatus newStatus) {
        Ride ride = getOrThrow(rideId);
        RideStatusTransition transition = new RideStatusTransition(ride.getStatus(), newStatus);
        transitionValidator.validate(transition, new BeanPropertyBindingResult(transition, transition.getClass().getSimpleName()));
        ride.setStatus(newStatus);
        return rideRepository.save(ride);
    }

}
