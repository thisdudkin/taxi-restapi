package by.dudkin.notification.controller;

import by.dudkin.notification.domain.RideRequest;
import by.dudkin.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static by.dudkin.notification.utils.TokenConstants.USERNAME_CLAIM_EXPRESSION;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(NotificationController.URI)
public class NotificationController {
    public static final String URI = "/api/notifications";
    private final NotificationService notificationService;

    @GetMapping
    public CompletableFuture<Set<RideRequest>> getNearbyRequests(@AuthenticationPrincipal(expression = USERNAME_CLAIM_EXPRESSION)
                                                                 String username,
                                                                 @RequestParam long timeout) {
        return notificationService.getNearbyRequestsForDriverAsync(username, timeout);
    }

}
