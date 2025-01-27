package by.dudkin.gateway;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static by.dudkin.gateway.FallbackUtils.AUTHENTICATION_FALLBACK_RESPONSE;
import static by.dudkin.gateway.FallbackUtils.DRIVER_FALLBACK_RESPONSE;
import static by.dudkin.gateway.FallbackUtils.NOTIFICATION_FALLBACK_RESPONSE;
import static by.dudkin.gateway.FallbackUtils.PASSENGER_FALLBACK_RESPONSE;
import static by.dudkin.gateway.FallbackUtils.PAYMENT_FALLBACK_RESPONSE;
import static by.dudkin.gateway.FallbackUtils.RIDES_FALLBACK_RESPONSE;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequestMapping(FallbackController.URI)
class FallbackController {

    static final String URI = "/fallback";

    @RequestMapping(value = "/auth", method = {GET, POST, PUT, DELETE, PATCH})
    ResponseEntity<ProblemDetail> authFallback() {
        return ResponseEntity.status(503).body(ProblemDetail.forStatusAndDetail(SERVICE_UNAVAILABLE, AUTHENTICATION_FALLBACK_RESPONSE));
    }

    @RequestMapping(value = "/driver", method = {GET, POST, PUT, DELETE, PATCH})
    ResponseEntity<ProblemDetail> driverFallback() {
        return ResponseEntity.status(503).body(ProblemDetail.forStatusAndDetail(SERVICE_UNAVAILABLE, DRIVER_FALLBACK_RESPONSE));
    }

    @RequestMapping(value = "/passenger", method = {GET, POST, PUT, DELETE, PATCH})
    ResponseEntity<ProblemDetail> passengerFallback() {
        return ResponseEntity.status(503).body(ProblemDetail.forStatusAndDetail(SERVICE_UNAVAILABLE, PASSENGER_FALLBACK_RESPONSE));
    }

    @RequestMapping(value = "/payment", method = {GET, POST, PUT, DELETE, PATCH})
    ResponseEntity<ProblemDetail> paymentFallback() {
        return ResponseEntity.status(503).body(ProblemDetail.forStatusAndDetail(SERVICE_UNAVAILABLE, PAYMENT_FALLBACK_RESPONSE));
    }

    @RequestMapping(value = "/rides", method = {GET, POST, PUT, DELETE, PATCH})
    ResponseEntity<ProblemDetail> ridesFallback() {
        return ResponseEntity.status(503).body(ProblemDetail.forStatusAndDetail(SERVICE_UNAVAILABLE, RIDES_FALLBACK_RESPONSE));
    }

    @RequestMapping(value = "/notification", method = {GET, POST, PUT, DELETE, PATCH})
    ResponseEntity<ProblemDetail> notificationFallback() {
        return ResponseEntity.status(503).body(ProblemDetail.forStatusAndDetail(SERVICE_UNAVAILABLE, NOTIFICATION_FALLBACK_RESPONSE));
    }

}
