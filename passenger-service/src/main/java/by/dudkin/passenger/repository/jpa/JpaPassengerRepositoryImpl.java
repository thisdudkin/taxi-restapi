package by.dudkin.passenger.repository.jpa;

import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.repository.PassengerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.dao.DataAccessException;
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

    @Override
    public Optional<Passenger> findById(long id) throws DataAccessException {
        return Optional.ofNullable(this.em.find(Passenger.class, id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Passenger> findAll() throws DataAccessException {
        return this.em.createQuery("SELECT passenger FROM Passenger passenger").getResultList();
    }

    @Override
    public void save(Passenger passenger) throws DataAccessException {
        if (passenger.getId() == null) {
            this.em.persist(passenger);
        } else {
            this.em.merge(passenger);
        }
    }

    @Override
    public void delete(Passenger passenger) throws DataAccessException {
        this.em.remove(this.em.contains(passenger) ? passenger : this.em.merge(passenger));
    }

}
