package by.dudkin.rides.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alexander Dudkin
 */
public final class TestJwtUtils {

    private static final Map<String, Jwt> tokenStorage = new ConcurrentHashMap<>();

    private TestJwtUtils() {}

    public static final String ROLE_PASSENGER = "ROLE_PASSENGER";
    public static final String ROLE_DRIVER = "ROLE_DRIVER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static JwtAuthenticationToken createJwtToken(String username, String... roles) {
        List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
            .map(SimpleGrantedAuthority::new)
            .toList();

        Map<String, Object> headers = Map.of("alg", "none");
        Map<String, Object> claims = Map.of(
            "preferred_username", username,
            "spring_sec_roles", Arrays.asList(roles)
        );

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(300), headers, claims);
        tokenStorage.put(jwt.getTokenValue(), jwt);

        return new JwtAuthenticationToken(jwt, authorities, username);
    }

    public static HttpHeaders createHeadersWithToken(String... roles) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JwtAuthenticationToken jwtToken = TestJwtUtils.createJwtToken("test-user", roles);
        headers.set("Authorization", "Bearer " + jwtToken.getToken().getTokenValue());

        return headers;
    }

    public static HttpHeaders createHeadersWithTokenAndUsername(String username, String... roles) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JwtAuthenticationToken jwtToken = TestJwtUtils.createJwtToken(username, roles);
        headers.set("Authorization", "Bearer " + jwtToken.getToken().getTokenValue());

        return headers;
    }

    public static Jwt parseToken(String tokenValue) {
        return tokenStorage.get(tokenValue);
    }

    public static void setMockAuthentication() {
        SecurityContextHolder.getContext()
            .setAuthentication(createJwtToken("mock-username", ROLE_PASSENGER));
    }

}
