package by.dudkin.auth;

import by.dudkin.auth.metric.MetricUtils;
import by.dudkin.auth.metric.TrackMetric;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequestMapping(AuthenticationController.URI)
class AuthenticationController {
    static final String URI = "/api/auth";
    final AuthenticationService authenticationService;

    AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    ResponseEntity<Void> registerUser(@RequestBody @Valid RegistrationRequest req) {
        authenticationService.saveUser(req);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/register/admin")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<Void> registerAdmin(@RequestBody @Valid RegistrationRequest req) {
        authenticationService.saveAdmin(req);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @TrackMetric(metricName = MetricUtils.LOGIN_COUNT)
    ResponseEntity<AccessTokenResponse> login(@RequestBody @Valid LoginRequest req) {
        return new ResponseEntity<>(authenticationService.login(req), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    ResponseEntity<AccessTokenResponse> refresh(@RequestParam String token) {
        return new ResponseEntity<>(authenticationService.refreshToken(token), HttpStatus.OK);
    }

}
