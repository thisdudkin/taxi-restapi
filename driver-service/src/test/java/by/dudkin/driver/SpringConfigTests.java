package by.dudkin.driver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Alexander Dudkin
 */
@ActiveProfiles("hsqldb")
@SpringBootTest(classes = DriverServiceApplication.class)
public class SpringConfigTests {

    @Test
    void contextLoads() {
    }

}
