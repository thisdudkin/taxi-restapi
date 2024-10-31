package by.dudkin.passenger.repository;

import by.dudkin.passenger.entity.Passenger;
import org.springframework.dao.DataAccessException;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Alexander Dudkin
 */
public interface PassengerRepository {

    Collection<Passenger> findAll();

    Optional<Passenger> findById(long id);

    void saveOrUpdate(Passenger passenger);

    void save(Passenger passenger);

    void delete(Passenger passenger);

}
