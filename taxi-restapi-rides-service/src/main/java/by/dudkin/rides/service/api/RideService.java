package by.dudkin.rides.service.api;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.AvailableDriver;
import by.dudkin.rides.rest.dto.request.PendingRide;
import by.dudkin.rides.rest.dto.response.RideResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Alexander Dudkin
 */
public interface RideService {

    RideResponse create(RideRequest rideRequest);
    RideResponse read(long rideId);
    PaginatedResponse<RideResponse> readAll(Specification<Ride> spec, Pageable pageable);
    RideResponse update(long rideId, RideRequest rideRequest);
    void delete(long rideId);

    RideResponse cancel(long rideId);
    RideResponse complete(long rideId);
    RideResponse activate(long rideId);
    RideResponse rate(long rideId, RideCompletionRequest request);
    RideResponse assign(long rideId, AvailableDriver availableDriver);
    Page<PendingRide> findAllPendingRides(Pageable pageable);

}
