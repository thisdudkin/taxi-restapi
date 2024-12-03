package by.dudkin.notification.service;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.notification.domain.RideRequest;
import by.dudkin.notification.service.dao.RideDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Set;
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
    private final ExecutorService threads = Executors.newCachedThreadPool();

    public DeferredResult<Set<RideRequest>> getNearbyRequestsForDriverAsync(Long driverId, long timeout) {
        DeferredResult<Set<RideRequest>> result = new DeferredResult<>();

        threads.submit(() -> {
            try {
                Set<RideRequest> lastRequests = rideDao.findNearbyRequestsForDriver(driverId);

                long endTime = System.currentTimeMillis() + timeout;
                while (System.currentTimeMillis() < endTime) {
                    Set<RideRequest> currentRequests = rideDao.findNearbyRequestsForDriver(driverId);
                    if (!currentRequests.equals(lastRequests)) {
                        result.setResult(currentRequests);
                        return;
                    }
                    TimeUnit.MILLISECONDS.sleep(1000);
                }

                result.setResult(lastRequests);
            } catch (Exception e) {
                result.setErrorResult(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.GENERAL_ERROR));
            }
        });

        return result;
    }

}
