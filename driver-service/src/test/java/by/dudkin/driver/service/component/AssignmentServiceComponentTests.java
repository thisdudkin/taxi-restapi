package by.dudkin.driver.service.component;

import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.driver.repository.AssignmentRepository;
import by.dudkin.driver.service.api.AssignmentService;
import by.dudkin.driver.util.TestDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest
@Testcontainers
@Transactional
@Sql("classpath:data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AssignmentServiceComponentTests {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    AssignmentService assignmentService;

    @Autowired
    AssignmentRepository assignmentRepository;

    private static final String COLOR_BLUE = "Blue";

    @Test
    void shouldFindAssignmentById() {
        // Act
        var response = assignmentService.findById(100L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.car().color()).isEqualTo(COLOR_BLUE);
    }

    @Test
    void shouldCreateAssignment() {
        // Arrange
        var assignmentRequest = TestDataGenerator.randomAssignmentRequestWithIds(104L, 106L);

        // Act
        var assignmentResponse = assignmentService.create(assignmentRequest);

        // Assert
        assertThat(assignmentResponse).isNotNull();
        assertThat(assignmentResponse.status()).isEqualTo(AssignmentStatus.ACTIVE);
        assertThat(assignmentResponse.car().id()).isEqualTo(106L);
        assertThat(assignmentResponse.driver().id()).isEqualTo(104L);
    }

    @Test
    void shouldCancelAssignment() {
        // Arrange
        long assignmentId = 101L;

        // Act
        var response = assignmentService.cancelAssignment(assignmentId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(AssignmentStatus.COMPLETED);
    }

    @Test
    void shouldDeleteAssignment() {
        // Act
        assignmentService.delete(101L);

        // Assert
        var deletedAssignment = assignmentRepository.findById(101L).orElse(null);
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
