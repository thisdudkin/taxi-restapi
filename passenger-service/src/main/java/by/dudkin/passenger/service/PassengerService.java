package by.dudkin.passenger.service;

import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface PassengerService {

    PassengerDto findById(long id);

    Collection<PassengerDto> findAll();

    PassengerDto create(PassengerFieldsDto passengerFieldsDto);

    PassengerDto update(long passengerId, PassengerFieldsDto passengerFieldsDto);

    void delete(long passengerId);

}
