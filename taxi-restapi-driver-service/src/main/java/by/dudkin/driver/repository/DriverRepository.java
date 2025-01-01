package by.dudkin.driver.repository;

import by.dudkin.driver.domain.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
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

}
