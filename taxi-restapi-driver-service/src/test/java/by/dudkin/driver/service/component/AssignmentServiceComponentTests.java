package by.dudkin.driver.service.component;

import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.driver.repository.AssignmentRepository;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.service.api.AssignmentService;
import by.dudkin.driver.util.TestDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.test.context.EmbeddedKafka;
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
@Sql("classpath:data.sql")
@ActiveProfiles({"test", "kafka"})
@EmbeddedKafka(partitions = 1, topics = {"available-drivers", "ride-requests"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AssignmentServiceComponentTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @MockBean
    DriverLocationRepository driverLocationRepository;

    @Autowired
    AssignmentService assignmentService;

    @Autowired
    AssignmentRepository assignmentRepository;

    private static final String COLOR_BLUE = "Blue";

    @Test
    void shouldFindAssignmentById() {
        // Act
        var response = assignmentService.findById(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981"));

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.car().color()).isEqualTo(COLOR_BLUE);
    }

    @Test
    void shouldCreateAssignment() {
        // Arrange
        var assignmentRequest = TestDataGenerator.randomAssignmentRequestWithIds(
            UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6985"),
            UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6985"));

        // Act
        var assignmentResponse = assignmentService.create(assignmentRequest);

        // Assert
        assertThat(assignmentResponse).isNotNull();
        assertThat(assignmentResponse.status()).isEqualTo(AssignmentStatus.ACTIVE);
        assertThat(assignmentResponse.car().id()).isNotNull();
        assertThat(assignmentResponse.driver().id()).isNotNull();
    }

    @Test
    void shouldCancelAssignment() {
        // Arrange
        UUID assignmentId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        var response = assignmentService.cancelAssignment(assignmentId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(AssignmentStatus.COMPLETED);
    }

    @Test
    void shouldDeleteAssignment() {
        // Arrange
        UUID id = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        assignmentService.delete(id);

        // Assert
        var deletedAssignment = assignmentRepository.findById(id).orElse(null);
        assertThat(deletedAssignment).isNull();
    }

    @Test
    void shouldReturnPaginatedResponse() {
        // Arrange
        var pageable = PageRequest.of(0, 2);

        // Act
        var paginatedResponse = assignmentService.findAll(null, pageable);

        // Assert
        assertThat(paginatedResponse).isNotNull();
        assertThat(paginatedResponse.getContent()).hasSize(2);
    }

}
