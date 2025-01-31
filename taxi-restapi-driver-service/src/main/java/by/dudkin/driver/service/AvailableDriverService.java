package by.dudkin.driver.service;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.domain.DriverLocationDocument;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.rest.advice.custom.NoAvailableDriverException;
import by.dudkin.driver.rest.dto.request.AvailableDriver;
import by.dudkin.driver.rest.dto.response.AvailableDriverResponse;
import by.dudkin.driver.rest.dto.response.PendingRide;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Alexander Dudkin
 */
@Repository
@RequiredArgsConstructor
public class AvailableDriverService {

    private final JdbcTemplate jdbcTemplate;
    private final DriverLocationRepository driverLocationRepository;

    public AvailableDriver findAvailableDriver(PendingRide ride) {
        Set<DriverLocationDocument> drivers = getDriverInRange(ride.from().getLat(), ride.from().getLng());
        AvailableDriver poorestDriver = findPoorestDriver(ride, drivers);
        if (poorestDriver == null) {
            throw new NoAvailableDriverException(ErrorMessages.AVAILABLE_DRIVER_NOT_FOUND);
        }

        return poorestDriver;
    }

    public AvailableDriverResponse findAvailableDriverByUsername(String username) {
        final AtomicReference<AvailableDriverResponse> result = new AtomicReference<>();
        jdbcTemplate.execute("select dca.driver_id, dca.car_id " +
                             "from driver_car_assignments dca " +
                             "join public.drivers d on dca.driver_id = d.id " +
                             "where d.username = ? and dca.status = 'ACTIVE'", (PreparedStatementCallback<?>) selectDriver -> {
            selectDriver.setString(1, username);
            try (ResultSet resultSet = selectDriver.executeQuery()) {
                if (resultSet.next()) {
                    UUID driverId = resultSet.getObject("driver_id", UUID.class);
                    UUID carId = resultSet.getObject("car_id", UUID.class);
                    AvailableDriverResponse response = new AvailableDriverResponse(driverId, carId);
                    result.set(response);
                }
            }
            return null;
        });

        return result.get();
    }

    private Set<DriverLocationDocument> getDriverInRange(BigDecimal lat, BigDecimal lng) {
        Set<DriverLocationDocument> driversInRange = driverLocationRepository.findDriversInRange(lat.doubleValue(), lng.doubleValue());
        if (driversInRange.isEmpty()) {
            throw new NoAvailableDriverException(ErrorMessages.AVAILABLE_DRIVER_NOT_FOUND);
        }

        return driversInRange;
    }

    private AvailableDriver findPoorestDriver(PendingRide ride, Set<DriverLocationDocument> drivers) {
        final AtomicReference<AvailableDriver> result = new AtomicReference<>();
        jdbcTemplate.execute("select dca.driver_id, dca.car_id " +
            "from driver_car_assignments dca " +
            "join public.drivers d on dca.driver_id = d.id " +
            "where d.status = 'READY' and dca.status = 'ACTIVE' and d.id = any(?::uuid[]) ", (PreparedStatementCallback<?>) selectDrivers -> {
            selectDrivers.setArray(1, selectDrivers.getConnection()
                .createArrayOf("uuid", drivers.stream().map(DriverLocationDocument::driverId).toArray()));
            try (ResultSet driverSet = selectDrivers.executeQuery()) {
                while (driverSet.next()) {
                    UUID driverId = driverSet.getObject("driver_id", UUID.class);
                    UUID carId = driverSet.getObject("car_id", UUID.class);
                    DriverLocationDocument driverLocation = drivers.stream()
                        .filter(doc -> doc.driverId().equals(driverId))
                        .findFirst().orElseThrow();
                    result.set(new AvailableDriver(ride.rideId(), driverId, carId,
                        BigDecimal.valueOf(driverLocation.location().getLat()),
                        BigDecimal.valueOf(driverLocation.location().getLon())
                    ));
                }
            }
            return null;
        });
        return result.get();
    }

}
