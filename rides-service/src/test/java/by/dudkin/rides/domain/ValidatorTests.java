package by.dudkin.rides.domain;

import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.common.enums.RideStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 * Simple test to make sure that Bean Validation is working
 * (useful when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
class ValidatorTests {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    private static final String COUNTRY = "Country";
    private static final String CITY = "City";
    private static final String STREET = "Street";

    @Test
    void shouldNotValidateWhenLocationIsNull() {

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Ride ride = Ride.builder()
                .passengerId(1L)
                .driverId(1L)
                .carId(1L)
                .from(null)
                .to(Location.builder()
                        .county(COUNTRY)
                        .city(CITY)
                        .street(STREET)
                        .build())
                .price(BigDecimal.ZERO)
                .startTime(Instant.now().plus(5, ChronoUnit.MINUTES))
                .endTime(Instant.now().plus(35, ChronoUnit.MINUTES))
                .paymentMethod(PaymentMethod.CASH)
                .build();

        Validator validator = createValidator();
        Set<ConstraintViolation<Ride>> constraintViolations = validator.validate(ride);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Ride> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("from");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

}
