package by.dudkin.promocode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
public class SchedulerImpl implements Scheduler {
    static final int PROMOCODES_COUNT = 100;

    final PromocodeRepository promocodeRepository;
    final Generator promocodeGenerator;

    private static final Logger logger = LoggerFactory.getLogger(SchedulerImpl.class);

    SchedulerImpl(PromocodeRepository promocodeRepository, Generator promocodeGenerator) {
        this.promocodeRepository = promocodeRepository;
        this.promocodeGenerator = promocodeGenerator;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void executeTask() {
        List<Promocode> newPromocodes = promocodeGenerator.generate(PROMOCODES_COUNT);

        try {
            promocodeRepository.deleteRecentPromocodes();
            promocodeRepository.savePromocodes(newPromocodes);

            logger.info("Promocodes successfully updated at: {}", LocalDateTime.now());
        } catch (Exception e) {
            logger.error("Failed to update promocodes -> {}", e.getMessage());
        }
    }

}
