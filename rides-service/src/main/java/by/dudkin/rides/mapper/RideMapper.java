package by.dudkin.rides.mapper;

import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.RideResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * @author Alexander Dudkin
 */
@Mapper(componentModel = "spring")
public interface RideMapper {

    RideResponse toResponse(Ride ride);

    Ride toRide(RideRequest rideRequest);

    void updateRide(RideRequest rideRequest, @MappingTarget Ride ride);

}
