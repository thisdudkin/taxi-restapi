package by.dudkin.passenger.mapper;

import by.dudkin.common.entity.BaseEntity;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
@Mapper(componentModel = "spring")
public interface PassengerMapper {
    Passenger toPassenger(PassengerDto passengerDto);

    Passenger toPassenger(PassengerFieldsDto passengerFieldsDto);

    @Mapping(target = "averageRating", source = "averageRating")
    PassengerDto toPassengerDto(Passenger passenger);

    Collection<PassengerDto> toPassengerDtos(Collection<Passenger> passengers);
}
