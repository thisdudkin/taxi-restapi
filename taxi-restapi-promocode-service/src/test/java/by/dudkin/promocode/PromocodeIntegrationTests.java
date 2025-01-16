package by.dudkin.promocode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static by.dudkin.promocode.TestJwtUtils.*;

/**
 * @author Alexander Dudkin
 */
@Testcontainers
@ActiveProfiles("test")
@Sql("classpath:data.sql")
@Import(TestSecurityConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PromocodeIntegrationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;

    static final String USERNAME = "username";

    @Test
    void shouldReturn401HttpStatusCode() {
        // Arrange
        String URI = PromocodeApi.URI.concat("?code=SPRING2024");

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, null, ProblemDetail.class);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturn403HttpStatusCode() {
        // Arrange
        String URI = PromocodeApi.URI.concat("/expired");
        HttpHeaders authHeaders = createHeadersWithToken(USERNAME, ROLE_PASSENGER);

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.DELETE, new HttpEntity<>(authHeaders), ProblemDetail.class);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void validatePromocode_ShouldReturnPromocode_WhenExists() {
        // Arrange
        HttpHeaders authHeaders = createHeadersWithToken(USERNAME, ROLE_PASSENGER);
        String URI = PromocodeApi.URI.concat("?code=SPRING2024");

        // Act
        ResponseEntity<Promocode> response = restTemplate.exchange(URI, HttpMethod.GET, new HttpEntity<>(authHeaders), Promocode.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("SPRING2024", response.getBody().code());
        assertEquals(15, response.getBody().discount());
    }

    @Test
    void validatePromocode_ShouldReturnNotFound_WhenNotFound() {
        // Arrange
        HttpHeaders authHeaders = createHeadersWithToken(USERNAME, ROLE_PASSENGER);
        String URI = PromocodeApi.URI.concat("?code=INVALID");

        // Act
        ResponseEntity<Promocode> response = restTemplate.exchange(URI, HttpMethod.GET, new HttpEntity<>(authHeaders), Promocode.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getActivePromocodes_ShouldReturnPagedResults() {
        // Arrange
        HttpHeaders authHeaders = createHeadersWithToken(USERNAME, ROLE_PASSENGER);
        String URI = PromocodeApi.URI.concat("/active?page=0&size=5");

        // Act
        ResponseEntity<Set<Promocode>> response = restTemplate.exchange(URI, HttpMethod.GET, new HttpEntity<>(authHeaders), new ParameterizedTypeReference<Set<Promocode>>() {});

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().size());
    }

}
