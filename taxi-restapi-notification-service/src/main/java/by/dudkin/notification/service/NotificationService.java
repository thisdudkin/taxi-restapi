package by.dudkin.notification.service;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.notification.domain.RideRequest;
import by.dudkin.notification.kafka.domain.AcceptedRide;
import by.dudkin.notification.service.dao.DriverDao;
import by.dudkin.notification.service.dao.RideDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RideDao rideDao;
    private final DriverDao driverDao;

    public CompletableFuture<Set<RideRequest>> getNearbyRequestsForDriverAsync(long driverId, long timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try {
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
