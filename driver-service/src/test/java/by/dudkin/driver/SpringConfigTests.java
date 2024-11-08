package by.dudkin.driver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest(classes = DriverServiceApplication.class)
public class SpringConfigTests {

    @Test
    void contextLoads() {
    }

}
