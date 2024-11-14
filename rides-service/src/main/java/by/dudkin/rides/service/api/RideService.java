package by.dudkin.rides.service.api;

import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.RideResponse;
import org.springframework.data.domain.Pageable;

/**
 * @author Alexander Dudkin
 */
public interface RideService {

    RideResponse create(RideRequest rideRequest);
    RideResponse read(long rideId);
    PaginatedResponse<RideResponse> readAll(Pageable pageable);
    RideResponse update(long rideId, RideRequest rideRequest);
    void delete(long rideId);

    RideResponse changeStatus(long rideId, RideStatus newStatus);
    RideResponse rate(long rideId, RideCompletionRequest request);

}
