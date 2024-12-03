package by.dudkin.notification.service.dao;

import by.dudkin.notification.domain.RideRequest;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Dudkin
 */
@Repository
@AllArgsConstructor
public class RideDao {

    private final JdbcTemplate jdbcTemplate;

    public void insertRideRequest(RideRequest request) {
        jdbcTemplate.execute("insert into ride_requests (ride_id, location, price) " +
                "values (?, st_setsrid(st_makepoint(?, ?), 4326), ?)",
            (PreparedStatementCallback<?>) ps -> {
                ps.setLong(1, request.rideId());
                ps.setBigDecimal(2, request.lng());
                ps.setBigDecimal(3, request.lat());
                ps.setBigDecimal(4, request.price());
                ps.execute();
                return null;
            });
    }

    public Set<RideRequest> findNearbyRequestsForDriver(Long driverId) {
        Set<RideRequest> requests = new HashSet<>();
        jdbcTemplate.execute("select driver_id, st_y(location::geometry) as lat, st_x(location::geometry) as lng " +
            "from available_drivers " +
            "where driver_id = ?", (PreparedStatementCallback<?>) selectDriver -> {
            selectDriver.setLong(1, driverId);
            try (ResultSet driverSet = selectDriver.executeQuery()) {
                if (driverSet.next()) {
                    jdbcTemplate.execute("select rr.id, rr.ride_id, st_y(location::geometry) as lat, st_x(location::geometry) as lng, rr.price " +
                        "from ride_requests rr " +
                        "where st_dwithin(" +
                        "   rr.location::geography, " +
                        "   st_setsrid(st_makepoint(?, ?), 4326)::geography, " +
                        "   3000" + // radius
                        ")", (PreparedStatementCallback<?>) selectRequests -> {
                        selectRequests.setBigDecimal(1, driverSet.getBigDecimal("lng"));
                        selectRequests.setBigDecimal(2, driverSet.getBigDecimal("lat"));
                        try (ResultSet requestSet = selectRequests.executeQuery()) {
                            while (requestSet.next()) {
                                requests.add(new RideRequest(
                                    requestSet.getLong("id"),
                                    requestSet.getLong("ride_id"),
                                    requestSet.getBigDecimal("lat"),
                                    requestSet.getBigDecimal("lng"),
                                    requestSet.getBigDecimal("price")
                                ));
                            }
                        }
                        return null;
                    });
                }
            }
            return null;
        });

        return Collections.unmodifiableSet(requests);
    }

}
