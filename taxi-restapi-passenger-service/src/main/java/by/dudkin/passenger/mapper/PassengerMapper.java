package by.dudkin.passenger.mapper;

import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
@Mapper(componentModel = "spring")
public interface PassengerMapper {

    @Mapping(target = "rating", source = "averageRating")
    @Mapping(target = "paymentMethod", source = "preferredPaymentMethod")
    PassengerResponse toResponse(Passenger passenger);

    @Mapping(source = "paymentMethod", target = "preferredPaymentMethod")
    Passenger toPassenger(PassengerRequest passengerRequest);

    @Mapping(source = "paymentMethod", target = "preferredPaymentMethod")
    Passenger toPassenger(PassengerResponse passengerResponse);

    @Mapping(source = "paymentMethod", target = "preferredPaymentMethod")
    void updatePassenger(PassengerRequest passengerRequest, @MappingTarget Passenger passenger);

    Collection<PassengerResponse> toPassengerDtos(Collection<Passenger> passengers);

}
