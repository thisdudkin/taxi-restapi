package by.dudkin.driver.util;

import by.dudkin.driver.domain.DriverCarAssignment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Component
public class AssignmentSpecification {

    public Specification<DriverCarAssignment> hasDriverId(UUID driverId) {
        return ((root, query, criteriaBuilder) ->
            driverId == null ? null : criteriaBuilder.equal(root.get("driver").get("id"), driverId));
    }

    public Specification<DriverCarAssignment> hasCarId(UUID carId) {
        return (root, query, criteriaBuilder) ->
            carId == null ? null : criteriaBuilder.equal(root.get("car").get("id"), carId);
    }

    public Specification<DriverCarAssignment> getSpecification(UUID driverId, UUID carId) {
        Specification<DriverCarAssignment> spec = Specification.where(null);
        if (driverId != null) {
            spec = spec.and(hasDriverId(driverId));
        }
        if (carId != null) {
            spec = spec.and(hasCarId(carId));
        }
        return spec;
    }

}
