package by.dudkin.payment.service;

import by.dudkin.common.util.TransactionRequest;
import by.dudkin.payment.service.dao.TransactionDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionDao transactionDao;
    private final AccountService accountService;

    @Transactional
    public void handleTransaction(TransactionRequest<UUID> req) throws SQLException {
        accountService.decreaseBalance(req.passengerId(), req.amount());
        accountService.topUpBalance(req.driverId(), req.amount());

        transactionDao.insertTransaction(req);
        log.info("Transaction for ride with ID: {} completed.", req.rideId());
    }

}
