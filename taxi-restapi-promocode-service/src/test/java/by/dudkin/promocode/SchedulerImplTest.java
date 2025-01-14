package by.dudkin.promocode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alexander Dudkin
 */
@ExtendWith(MockitoExtension.class)
class SchedulerImplTest {

    @Mock
    private PromocodeRepository promocodeRepository;

    @Mock
    private Generator promocodeGenerator;

    private Scheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new SchedulerImpl(promocodeRepository, promocodeGenerator);
    }

    @Test
    void executeTask_ShouldGenerateAndSaveNewPromocodes() {
        // Arrange
        List<Promocode> mockPromocodes = List.of(
            new Promocode(UUID.randomUUID(), "CODECODE", 10, LocalDateTime.now()),
            new Promocode(UUID.randomUUID(), "COD3COD3", 11, LocalDateTime.now())
        );
        when(promocodeGenerator.generate(SchedulerImpl.PROMOCODES_COUNT)).thenReturn(mockPromocodes);

        // Act
        scheduler.executeTask();

        // Assert
        verify(promocodeRepository).deleteRecentPromocodes();
        verify(promocodeRepository).savePromocodes(mockPromocodes);
    }

    @Test
    void executeTask_ShouldHandleExceptionGracefully() {
        // Arrange
        when(promocodeGenerator.generate(anyInt())).thenReturn(List.of(new Promocode(UUID.randomUUID(), "CODECODE", 10, LocalDateTime.now())));
        doThrow(new RuntimeException("Database error")).when(promocodeRepository).deleteRecentPromocodes();

        // Act
        scheduler.executeTask();

        // Assert
        verify(promocodeRepository, never()).savePromocodes(anyList());
    }

}
