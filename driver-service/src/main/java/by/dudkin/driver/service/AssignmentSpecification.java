package by.dudkin.driver.service;

import by.dudkin.driver.domain.DriverCarAssignment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component
public class AssignmentSpecification {

    public Specification<DriverCarAssignment> hasDriverId(Long driverId) {
        return ((root, query, criteriaBuilder) ->
            driverId == null ? null : criteriaBuilder.equal(root.get("driver").get("id"), driverId));
    }

    public Specification<DriverCarAssignment> hasCarId(Long carId) {
        return (root, query, criteriaBuilder) ->
            carId == null ? null : criteriaBuilder.equal(root.get("car").get("id"), carId);
    }

    public Specification<DriverCarAssignment> getSpecification(Long driverId, Long carId) {
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
