package by.dudkin.passenger.service;

import by.dudkin.passenger.entity.Passenger;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface PassengerService {

    Passenger findById(long id) throws DataAccessException;
    Collection<Passenger> findAll() throws DataAccessException;
    void save(Passenger passenger) throws DataAccessException;
    void delete(Passenger passenger) throws DataAccessException;

}
