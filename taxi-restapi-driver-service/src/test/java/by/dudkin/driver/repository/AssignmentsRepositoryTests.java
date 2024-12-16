package by.dudkin.driver.repository;

import by.dudkin.common.enums.AssignmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 */
@DataJpaTest
@Testcontainers
@Sql("classpath:data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AssignmentsRepositoryTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    AssignmentRepository assignmentRepository;

    @Test
    void testFindWithDriverAndCarById() {
        // Arrange
        UUID assignmentId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        var assignment = assignmentRepository.findWithDriverAndCarById(assignmentId);

        // Assert
        assertThat(assignment).isPresent();
        assertThat(assignment.get().getCar().getModel()).isNotEmpty();
        assertThat(assignment.get().getDriver().getInfo().getFirstName()).isNotEmpty();
    }

    @Test
    void testFindWithDriverAndCarByIdShouldFailed() {
        // Arrange
        UUID undefinedId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6911");

        // Act
        var unknown = assignmentRepository.findWithDriverAndCarById(undefinedId);

        // Arrange
        assertThat(unknown).isEmpty();
    }

    @Test
    void testFindActiveAssignmentByCarId() {
        // Arrange
        UUID carId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        var assignment = assignmentRepository.findActiveAssignmentByCarId(carId);

        // Assert
        assertThat(assignment).isPresent();
        assertThat(assignment.get().getDriver()).isNotNull();
        assertThat(assignment.get().getCar()).isNotNull();
        assertThat(assignment.get().getStatus()).isEqualTo(AssignmentStatus.ACTIVE);
    }

    @Test
    void testFindActiveAssignmentByCarIdShouldFailed() {
        // Arrange
        UUID undefinedId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6911");

        // Act
        var unknown = assignmentRepository.findActiveAssignmentByCarId(undefinedId);

        // Assert
        assertThat(unknown).isEmpty();
    }

}
