package by.dudkin.rides.repository;

import by.dudkin.rides.domain.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alexander Dudkin
 */
public interface RideRepository extends JpaRepository<Ride, Long> {
}
