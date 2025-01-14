package by.dudkin.promocode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alexander Dudkin
 */
@ExtendWith(MockitoExtension.class)
class PromocodeServiceImplTest {

    @Mock
    private PromocodeRepository promocodeRepository;

    @InjectMocks
    private PromocodeServiceImpl promocodeService;

    @Test
    void validate_ShouldReturnPromocode_WhenCodeExists() {
        // Arrange
        String code = "TEST1EST";
        Promocode expectedPromocode = new Promocode(UUID.randomUUID(), code, 19, LocalDateTime.now());
        when(promocodeRepository.getByCode(code)).thenReturn(Optional.of(expectedPromocode));

        // Act
        Promocode result = promocodeService.validate(code);

        // Assert
        assertSame(expectedPromocode, result);
    }

    @Test
    void validate_ShouldThrowException_WhenCodeNotFound() {
        // Arrange
        String code = "INVALID";
        when(promocodeRepository.getByCode(code)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PromocodeNotFoundException.class, () -> promocodeService.validate(code));
    }

    @Test
    void getActivePromocodes_ShouldReturnPromocodes() {
        // Arrange
        int page = 0, size = 10;
        Set<Promocode> expectedPromocodes = new HashSet<>();
        when(promocodeRepository.getActivePromocodes(page, size)).thenReturn(expectedPromocodes);

        // Act
        Set<Promocode> result = promocodeService.getActivePromocodes(page, size);

        // Assert
        assertSame(expectedPromocodes, result);
    }

    @Test
    void deleteExpiredPromocodes_ShouldCallRepository() {
        // Act
        promocodeService.deleteExpiredPromocodes();

        // Assert
        verify(promocodeRepository).deleteRecentPromocodes();
    }

}
