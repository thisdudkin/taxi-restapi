package by.dudkin.driver.repository;

import by.dudkin.driver.domain.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexander Dudkin
 */
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @EntityGraph(value = "driver-assignments-cars")
    Optional<Driver> findWithAssignmentsAndCarsById(Long id);

    @EntityGraph(value = "driver-assignments-cars")
    Page<Driver> findAll(Pageable pageable);

    @EntityGraph(value = "driver-assignments-cars")
    List<Driver> findAll();

}
