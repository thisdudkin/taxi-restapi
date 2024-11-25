package by.dudkin.rides.service;

import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.mapper.RideMapper;
import by.dudkin.rides.repository.RideRepository;
import by.dudkin.rides.rest.advice.custom.RideNotFoundException;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.RideResponse;
import by.dudkin.rides.service.api.RideService;
import by.dudkin.rides.utils.GeospatialUtils;
import by.dudkin.rides.utils.RideValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideMapper rideMapper;
    private final RideRepository rideRepository;
    private final PriceCalculator priceCalculator;
    private final RideValidation rideValidation;

    @Override
    public RideResponse create(RideRequest req) {
        Ride ride = rideMapper.toRide(req);
        ride.setPrice(priceCalculator.calculatePrice(GeospatialUtils.calculateDistance(
                req.from().getLat(), req.from().getLng(),
                req.to().getLat(), req.to().getLng())
        ));
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    @Override
    @Transactional(readOnly = true)
    public RideResponse read(long rideId) {
        return rideMapper.toResponse(getOrThrow(rideId));
    }

    @Override
    public RideResponse update(long rideId, RideRequest rideRequest) {
        Ride rideForUpdate = getOrThrow(rideId);
        rideMapper.updateRide(rideRequest, rideForUpdate);
        rideRepository.save(rideForUpdate);
        return rideMapper.toResponse(rideForUpdate);
    }

    @Override
    public void delete(long rideId) {
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
    public RideResponse cancel(long rideId) {
        Ride ride = getOrThrow(rideId);
        rideValidation.validateStatusTransition(ride.getStatus(), RideStatus.CANCEL);
        ride.setStatus(RideStatus.CANCEL);
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    @Override
    public RideResponse complete(long rideId) {
        Ride ride = getOrThrow(rideId);
        rideValidation.validateStatusTransition(ride.getStatus(), RideStatus.DONE);
        ride.setStatus(RideStatus.DONE);
        ride.setEndTime(LocalDateTime.now());
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    @Override
    public RideResponse activate(long rideId) {
        Ride ride = getOrThrow(rideId);
        rideValidation.validateStatusTransition(ride.getStatus(), RideStatus.ACTIVE);
        ride.setStatus(RideStatus.ACTIVE);
        ride.setStartTime(LocalDateTime.now());
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    @Override
    public RideResponse rate(long rideId, RideCompletionRequest request) {
        Ride ride = getOrThrow(rideId);
        ride.setRating(request.rating());
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    Ride getOrThrow(long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(ErrorMessages.RIDE_NOT_FOUND));
    }

}
