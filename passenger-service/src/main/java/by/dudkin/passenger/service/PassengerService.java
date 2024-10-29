package by.dudkin.passenger.service;

import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface PassengerService {

    PassengerDto findById(long id) throws DataAccessException;

    Collection<PassengerDto> findAll() throws DataAccessException;

    PassengerDto create(PassengerFieldsDto passengerFieldsDto) throws DataAccessException;

    PassengerDto update(Integer passengerId, PassengerFieldsDto passengerFieldsDto) throws DataAccessException;

    void delete(Long passengerId) throws DataAccessException;

}
