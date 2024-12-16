package by.dudkin.payment.service;

import by.dudkin.common.util.TransactionRequest;
import by.dudkin.payment.feign.DriverClient;
import by.dudkin.payment.feign.PassengerClient;
import by.dudkin.payment.service.dao.TransactionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Alexander Dudkin
 */
@Service
@EnableAsync
@RequiredArgsConstructor
public class TransactionService {

    private final DriverClient driverClient;
    private final PassengerClient passengerClient;

    private final TransactionDao transactionDao;

    @Transactional
    public void handleTransaction(TransactionRequest<Long> req) throws SQLException {
        CompletableFuture<Void> updateDriverBalance = updateDriverBalance(req.driverId(), req.amount());
        CompletableFuture<Void> updatePassengerBalance = updatePassengerBalance(req.passengerId(), req.amount());

        CompletableFuture.allOf(updateDriverBalance, updatePassengerBalance).join();
        transactionDao.insertTransaction(req);
    }

    @Async
    CompletableFuture<Void> updateDriverBalance(long driverId, BigDecimal amount) {
        driverClient.updateBalance(driverId, amount);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    CompletableFuture<Void> updatePassengerBalance(long passengerId, BigDecimal amount) {
        passengerClient.updateBalance(passengerId, amount);
        return CompletableFuture.completedFuture(null);
    }

}
