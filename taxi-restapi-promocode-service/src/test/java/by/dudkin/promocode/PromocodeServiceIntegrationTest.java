package by.dudkin.promocode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Sql("classpath:data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PromocodeServiceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private PromocodeService promocodeService;

    @Test
    void validate_ShouldReturnPromocode_WhenExists() {
        // Act
        Promocode result = promocodeService.validate("SUMMER50");

        // Assert
        assertNotNull(result);
        assertEquals("SUMMER50", result.code());
        assertEquals(50, result.discount());
    }

    @Test
    void validate_ShouldThrowException_WhenNotExists() {
        // Act & Assert
        assertThrows(PromocodeNotFoundException.class, () -> promocodeService.validate("INVALID"));
    }

    @Test
    void getActivePromocodes_ShouldReturnCorrectPageSize() {
        // Act
        Set<Promocode> result = promocodeService.getActivePromocodes(0, 5);

        // Assert
        assertEquals(5, result.size());
    }

}
