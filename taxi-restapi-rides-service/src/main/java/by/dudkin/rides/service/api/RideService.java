package by.dudkin.rides.service.api;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideCostRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.AvailableDriver;
import by.dudkin.rides.rest.dto.request.PendingRide;
import by.dudkin.rides.rest.dto.response.RideCostResponse;
import by.dudkin.rides.rest.dto.response.RideResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface RideService {

    RideResponse create(RideRequest rideRequest, String username);
    RideResponse read(UUID rideId);
    PaginatedResponse<RideResponse> readAll(Specification<Ride> spec, Pageable pageable);
    RideResponse update(UUID rideId, RideRequest rideRequest);
    void delete(UUID rideId);

    RideResponse cancel(UUID rideId);
    RideResponse complete(UUID rideId);
    RideResponse activate(UUID rideId);
    RideResponse rate(UUID rideId, RideCompletionRequest request);
    RideResponse assign(UUID rideId, String username);

    RideCostResponse checkCost(RideCostRequest request);

}
