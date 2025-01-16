package by.dudkin.driver.domain;

import by.dudkin.driver.rest.dto.request.CarRequest;
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
        var request = new CarRequest(
                "License",
                "Model",
                null,
                2017,
                "Color"
        );

        Validator validator = createValidator();
        Set<ConstraintViolation<CarRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<CarRequest> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("type");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

}
