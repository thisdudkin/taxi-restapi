package by.dudkin.payment.service;

import by.dudkin.common.util.TransactionRequest;
import by.dudkin.payment.feign.DriverClient;
import by.dudkin.payment.feign.PassengerClient;
import by.dudkin.payment.service.dao.TransactionDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
public class TransactionService {

    private final DriverClient driverClient;
    private final PassengerClient passengerClient;

    private final TransactionDao transactionDao;

    @Transactional
    public void handleTransaction(TransactionRequest<UUID> req) throws SQLException {
        try {
            driverClient.updateBalance(req.driverId(), req.amount());
            passengerClient.updateBalance(req.passengerId(), req.amount());
            transactionDao.insertTransaction(req);
        } catch (Exception e) {
            log.error("Transaction failed -> {}", req, e);
            throw e;
        }
    }

}
