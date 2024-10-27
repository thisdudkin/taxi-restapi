package by.dudkin.passenger.repository;

import by.dudkin.passenger.entity.Passenger;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface PassengerRepository {

    Collection<Passenger> findAll() throws DataAccessException;

    Passenger findById(long id) throws DataAccessException;

    void save(Passenger passenger) throws DataAccessException;

    void delete(Passenger passenger) throws DataAccessException;

}
