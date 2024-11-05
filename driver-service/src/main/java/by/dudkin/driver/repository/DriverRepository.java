package by.dudkin.driver.repository;

import by.dudkin.driver.domain.Driver;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Alexander Dudkin
 */
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @EntityGraph(value = "driver-assignments-cars")
    Optional<Driver> findWithAssignmentsAndCarsById(Long id);

}
