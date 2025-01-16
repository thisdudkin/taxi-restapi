package by.dudkin.promocode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alexander Dudkin
 */
@ExtendWith(MockitoExtension.class)
class PromocodeGeneratorTests {

    private Generator generator;

    @BeforeEach
    void setUp() {
        generator = new PromocodeGenerator();
    }

    @Test
    void generate_ShouldCreateSpecifiedNumberOfPromocodes() {
        // Arrange
        int count = 5;

        // Act
        List<Promocode> result = generator.generate(count);

        // Assert
        assertEquals(count, result.size());
        assertTrue(result.stream().allMatch(p -> p.code().length() == PromocodeUtils.CODE_LENGTH));
        assertTrue(result.stream().allMatch(p -> p.discount() >= PromocodeUtils.MIN_DISCOUNT
                                                 && p.discount() <= PromocodeUtils.MAX_DISCOUNT));
    }

    @Test
    void generate_ShouldReturnUnmodifiableList() {
        // Act
        List<Promocode> result = generator.generate(1);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(new Promocode(UUID.randomUUID(), "CODECODE", 12, LocalDateTime.now())));
    }

}
