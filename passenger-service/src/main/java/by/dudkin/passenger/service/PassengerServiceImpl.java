package by.dudkin.passenger.service;

import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author Alexander Dudkin
 */
@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    @Override
    @Transactional(readOnly = true)
    public Passenger findById(long id) throws DataAccessException {
        try {
            return passengerRepository.findById(id);
        } catch (ObjectRetrievalFailureException | EmptyResultDataAccessException e) {
            // Just ignore not found exception for Jpa realization
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Passenger> findAll() throws DataAccessException {
        return passengerRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Passenger passenger) throws DataAccessException {
        passengerRepository.save(passenger);
    }

    @Override
    @Transactional
    public void delete(Passenger passenger) throws DataAccessException {
        passengerRepository.delete(passenger);
    }

}
