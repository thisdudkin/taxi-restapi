package by.dudkin.rides.utils;

import by.dudkin.rides.domain.Ride;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component
public class RideSpecification {

    public Specification<Ride> hasPassengerId(Long passengerId) {
        return (root, query, criteriaBuilder) ->
            passengerId == null ? null : criteriaBuilder.equal(root.get("passenger").get("id"), passengerId);
    }

    public Specification<Ride> hasDriverId(Long driverId) {
        return ((root, query, criteriaBuilder) ->
            driverId == null ? null : criteriaBuilder.equal(root.get("driver").get("id"), driverId));
    }

    public Specification<Ride> hasCarId(Long carId) {
        return (root, query, criteriaBuilder) ->
            carId == null ? null : criteriaBuilder.equal(root.get("car").get("id"), carId);
    }

    public Specification<Ride> getSpecification(Long passengerId, Long driverId, Long carId) {
        Specification<Ride> spec = Specification.where(null);
        if (passengerId != null) {
            spec = spec.and(hasPassengerId(passengerId));
        }
        if (driverId != null) {
            spec = spec.and(hasDriverId(driverId));
        }
        if (carId != null) {
            spec = spec.and(hasCarId(carId));
        }
        return spec;
    }

}
