package by.dudkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Alexander Dudkin
 */
@EnableScheduling
@SpringBootApplication
public class PromocodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromocodeApplication.class, args);
    }

}
