package by.dudkin.notification.service;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.notification.domain.RideRequest;
import by.dudkin.notification.kafka.domain.AcceptedRide;
import by.dudkin.notification.metric.MetricUtils;
import by.dudkin.notification.service.dao.DriverDao;
import by.dudkin.notification.service.dao.RideDao;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Service
public class NotificationService {

    private final RideDao rideDao;
    private final DriverDao driverDao;

    private final AtomicInteger activeLongPollingConnections = new AtomicInteger(0);

    public NotificationService(RideDao rideDao, DriverDao driverDao, MeterRegistry meterRegistry) {
        this.rideDao = rideDao;
        this.driverDao = driverDao;
        Gauge.builder(MetricUtils.ACTIVE_LONG_POLLING_METRIC, this, NotificationService::getActiveLongPollingConnections)
            .register(meterRegistry);
    }

    private int getActiveLongPollingConnections() {
        return activeLongPollingConnections.get();
    }

    private void incrementActiveLongPollingConnections() {
        activeLongPollingConnections.incrementAndGet();
    }

    private void decrementActiveLongPollingConnections() {
        activeLongPollingConnections.decrementAndGet();
    }

    public CompletableFuture<Set<RideRequest>> getNearbyRequestsForDriverAsync(UUID driverId, long timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                incrementActiveLongPollingConnections();

                Set<RideRequest> lastRequests = rideDao.findNearbyRequestsForDriver(driverId);

                long endTime = System.currentTimeMillis() + timeout;
                while (System.currentTimeMillis() < endTime) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException("Task was cancelled");
                    }

                    Set<RideRequest> currentRequests = rideDao.findNearbyRequestsForDriver(driverId);
                    if (!currentRequests.equals(lastRequests)) {
                        return currentRequests;
                    }
                    TimeUnit.MILLISECONDS.sleep(1000);
                }

                return lastRequests;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Execution interrupted", e);
            } catch (Exception e) {
                throw new RuntimeException(ErrorMessages.GENERAL_ERROR);
            } finally {
                decrementActiveLongPollingConnections();
            }
        });
    }

    public void removeAcceptedRide(AcceptedRide ride) {
        try {
            rideDao.removeAcceptedRide(ride.rideId());
            driverDao.removeAvailableDriver(ride.driverId(), ride.carId());

            log.info("Successfully removed accepted ride with ID: {} and associated driver", ride.rideId());
        } catch (DataAccessException e) {
            log.error("Database error while removing accepted ride with ID: {} - {}", ride.rideId(), e.getMessage());
            if (e instanceof EmptyResultDataAccessException) {
                log.warn("Ride or driver already removed or not found for ride ID: {}", ride.rideId());
            } else {
                throw new RuntimeException("Failed to remove accepted ride due to database error", e);
            }
        }
    }

}
