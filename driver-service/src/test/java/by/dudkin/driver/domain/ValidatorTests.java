package by.dudkin.driver.domain;

import by.dudkin.common.enums.CarType;
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
        Car car = Car.builder()
                .licensePlate("4324EM-6")
                .model("")
                .type(CarType.PREMIUM)
                .year(2015)
                .color("White")
                .build();

        Validator validator = createValidator();
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Car> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("model");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }

}
