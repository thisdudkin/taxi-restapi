package by.dudkin.passenger.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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

    @Test
    void shouldNotValidateWhenUsernameEmpty() {

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Passenger passenger = Passenger.builder()
            .username("")
            .email("smth@gmail.com")
            .password("password")
            .build();

        Validator validator = createValidator();
        Set<ConstraintViolation<Passenger>> constraintViolations = validator.validate(passenger);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Passenger> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

}
