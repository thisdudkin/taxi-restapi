package by.dudkin.passenger.repository;

import by.dudkin.passenger.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alexander Dudkin
 */
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
