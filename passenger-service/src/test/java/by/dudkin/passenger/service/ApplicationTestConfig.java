package by.dudkin.passenger.service;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;

/**
 * @author Alexander Dudkin
 */
@TestConfiguration
public class ApplicationTestConfig {

    public ApplicationTestConfig() {
        MockitoAnnotations.openMocks(this);
    }

}
