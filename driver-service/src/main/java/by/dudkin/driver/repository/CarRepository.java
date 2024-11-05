package by.dudkin.driver.repository;

import by.dudkin.driver.domain.Car;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Alexander Dudkin
 */
public interface CarRepository extends JpaRepository<Car, Long> {

    @EntityGraph(value = "car-assignments-drivers")
    Optional<Car> findWithAssignmentsAndDriversById(Long id);

}
