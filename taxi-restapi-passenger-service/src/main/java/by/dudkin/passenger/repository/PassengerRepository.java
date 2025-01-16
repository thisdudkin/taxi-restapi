package by.dudkin.passenger.repository;

import by.dudkin.passenger.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface PassengerRepository extends JpaRepository<Passenger, UUID> {
    Optional<Passenger> findByUsername(String username);
}
