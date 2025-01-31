package by.dudkin.driver.repository;

import by.dudkin.driver.domain.Driver;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface DriverRepository extends JpaRepository<Driver, UUID> {

    @EntityGraph(value = "driver-assignments-cars")
    Optional<Driver> findByUsername(String username);

    @EntityGraph(value = "driver-assignments-cars")
    Optional<Driver> findWithAssignmentsAndCarsById(UUID id);

    @Modifying
    @Query(value = "insert into driver_ratings (driver_id, rating) values (:driverId, :rating)", nativeQuery = true)
    void rateDriver(UUID driverId, int rating);

}
