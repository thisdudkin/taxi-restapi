package by.dudkin.passenger.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest
@ActiveProfiles({"jpa", "hsqldb"})
class PassengerServiceJpaTests extends AbstractPassengerServiceTests {

}
