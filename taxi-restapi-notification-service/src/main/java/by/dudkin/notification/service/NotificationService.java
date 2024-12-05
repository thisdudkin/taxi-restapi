package by.dudkin.notification.service;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.notification.domain.RideRequest;
import by.dudkin.notification.service.dao.RideDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Alexander Dudkin
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RideDao rideDao;

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

}
