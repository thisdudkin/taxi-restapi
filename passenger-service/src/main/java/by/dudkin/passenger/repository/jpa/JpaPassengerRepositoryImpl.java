package by.dudkin.passenger.repository.jpa;

import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.repository.PassengerRepository;
import by.dudkin.passenger.util.ErrorMessages;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Alexander Dudkin
 */
@Repository
public class JpaPassengerRepositoryImpl implements PassengerRepository {

    @PersistenceContext
    private EntityManager em;

    private static final Logger logger = LoggerFactory.getLogger(JpaPassengerRepositoryImpl.class);

    @Override
    public Optional<Passenger> findById(long id) {
        try {
            return Optional.ofNullable(this.em.find(Passenger.class, id));
        } catch (DataAccessException e) {
            logger.error("Error finding passenger by ID: {}", id, e);
            throw new DataIntegrityViolationException(ErrorMessages.DATA_INTEGRITY);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Passenger> findAll() {
        try {
            return this.em.createQuery("SELECT passenger FROM Passenger passenger").getResultList();
        } catch (DataAccessException e) {
            logger.error("Error retrieving all passengers", e);
            throw new DataIntegrityViolationException(ErrorMessages.DATA_INTEGRITY);
        }
    }

    @Override
    public void saveOrUpdate(Passenger passenger) {
        try {
            if (passenger.getId() == null) {
                this.em.persist(passenger);
            } else {
                this.em.merge(passenger);
            }
        } catch (DataAccessException e) {
            logger.error("Error saving or updating passenger: {}", passenger);
            throw new DataIntegrityViolationException(ErrorMessages.DATA_INTEGRITY);
        }
    }

    @Override
    public void save(Passenger passenger) {
        try {
            this.em.persist(passenger);
        } catch (DataAccessException e) {
            logger.error("Error saving passenger: {}", passenger);
            throw new DataIntegrityViolationException(ErrorMessages.DATA_INTEGRITY);
        }
    }

    @Override
    public void delete(Passenger passenger) {
        try {
            this.em.remove(this.em.contains(passenger) ? passenger : this.em.merge(passenger));
        } catch (DataAccessException e) {
            logger.error("Error deleting passenger: {}", passenger, e);
            throw new DataIntegrityViolationException(ErrorMessages.DATA_INTEGRITY);
        }
    }

}
