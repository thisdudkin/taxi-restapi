package by.dudkin.promocode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Alexander Dudkin
 */
@Testcontainers
@ActiveProfiles("test")
@Sql("classpath:data.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PromocodeIntegrationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void validatePromocode_ShouldReturnPromocode_WhenExists() {
        // Arrange
        String URI = PromocodeApi.URI.concat("?code=SPRING2024");

        // Act
        ResponseEntity<Promocode> response = restTemplate.exchange(URI, HttpMethod.GET, null, Promocode.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("SPRING2024", response.getBody().code());
        assertEquals(15, response.getBody().discount());
    }

    @Test
    void validatePromocode_ShouldReturnNotFound_WhenNotFound() {
        // Arrange
        String URI = PromocodeApi.URI.concat("?code=INVALID");

        // Act
        ResponseEntity<Promocode> response = restTemplate.exchange(URI, HttpMethod.GET, null, Promocode.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getActivePromocodes_ShouldReturnPagedResults() {
        // Arrange
        String URI = PromocodeApi.URI.concat("/active?page=0&size=5");

        // Act
        ResponseEntity<Set<Promocode>> response = restTemplate.exchange(URI, HttpMethod.GET, null, new ParameterizedTypeReference<Set<Promocode>>() {
        });

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().size());
    }

}
