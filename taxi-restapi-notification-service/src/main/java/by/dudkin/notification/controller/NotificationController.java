package by.dudkin.notification.controller;

import by.dudkin.notification.domain.RideRequest;
import by.dudkin.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(NotificationController.URI)
public class NotificationController {
    public static final String URI = "/api/notifications";
    private final NotificationService notificationService;

    @GetMapping("/requests/{driverId}")
    public CompletableFuture<Set<RideRequest>> getNearbyRequests(@PathVariable long driverId, @RequestParam long timeout) {
        return notificationService.getNearbyRequestsForDriverAsync(driverId, timeout);
    }

}
