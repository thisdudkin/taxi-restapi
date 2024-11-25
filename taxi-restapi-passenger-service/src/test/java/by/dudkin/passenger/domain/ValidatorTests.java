package by.dudkin.passenger.domain;

import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.util.TestDataGenerator;
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
    void shouldNotValidateWhenModelEmpty() {

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        var request = TestDataGenerator.randomRequestWithInfo(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<PassengerRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<PassengerRequest> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("info");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

}
