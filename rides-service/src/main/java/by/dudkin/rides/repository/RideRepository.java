package by.dudkin.rides.repository;

import by.dudkin.rides.domain.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Alexander Dudkin
 */
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findAllByPassengerId(long passengerId);

    List<Ride> findAllByDriverId(long driverId);

    List<Ride> findAllByCarId(long carId);
}
