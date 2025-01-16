package by.dudkin.driver.service.component;

import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.repository.DriverRepository;
import by.dudkin.driver.service.api.DriverService;
import by.dudkin.driver.util.TestDataGenerator;
import by.dudkin.driver.util.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest
@Testcontainers
@Transactional
@Import(TestSecurityConfig.class)
@ActiveProfiles({"test", "kafka"})
@EmbeddedKafka(partitions = 1, topics = {"available-drivers", "ride-requests"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DriverServiceComponentTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @MockBean
    DriverLocationRepository driverLocationRepository;

    @MockBean(name = "testFilterChain")
    SecurityFilterChain testFilterChain;

    @Autowired
    DriverService driverService;

    @Autowired
    DriverRepository driverRepository;

    private static final String JOHN = "John";
    private static final String UPDATED_FIRSTNAME = "updated";

    @Test
    @Sql("classpath:data.sql")
    void shouldFindDriverById() {
        // Act
        var response = driverService.findById(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981"));

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.info().getFirstName()).isEqualTo(JOHN);
        assertThat(response.experience()).isEqualTo(5);
    }

    @Test
    void shouldCreateDriver() {
        // Arrange
        var driverRequest = TestDataGenerator.randomDriverRequest();

        // Act
        var driverResponse = driverService.create(driverRequest, "x-username");

        // Assert
        assertThat(driverResponse).isNotNull();
        assertThat(driverResponse.info().getFirstName()).isNotEmpty();
        assertThat(driverResponse.balance()).isNotZero();
    }

    @Test
    @Sql("classpath:data.sql")
    void shouldUpdateDriver() {
        // Arrange
        var updatedRequest = TestDataGenerator.randomDriverRequestWithFirstname(UPDATED_FIRSTNAME);

        // Act
        var response = driverService.update(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6983"), updatedRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.info().getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
    }

    @Test
    @Sql("classpath:data.sql")
    void shouldDeleteDriver() {
        // Act
        driverService.delete(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6983"));

        // Assert
        var deletedDriver = driverRepository.findById(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6983")).orElse(null);
        assertThat(deletedDriver).isNull();
    }

    @Test
    @Sql("classpath:data.sql")
    void shouldReturnPaginatedResponse() {
        // Arrange
        var pageable = PageRequest.of(0, 2);

        // Act
        var paginatedResponse = driverService.findAll(pageable);

        // Assert
        assertThat(paginatedResponse).isNotNull();
        assertThat(paginatedResponse.getContent()).hasSize(2);
    }

}
