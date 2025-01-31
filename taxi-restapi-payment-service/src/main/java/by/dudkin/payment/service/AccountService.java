package by.dudkin.payment.service;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.payment.dto.AccountRequest;
import by.dudkin.payment.dto.AccountResponse;
import by.dudkin.payment.dto.UserType;
import by.dudkin.payment.service.dao.AccountDao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Service
public class AccountService {

    private final AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void save(AccountRequest accountRequest, UserType userType) throws SQLException {
        accountDao.insertAccount(accountRequest, userType);
    }

    public BalanceResponse<UUID> getBalance(UUID id) throws SQLException {
        return accountDao.getBalance(id);
    }

    public void topUpBalance(UUID userId, BigDecimal amount) {
        accountDao.topUpBalance(userId, amount);
    }

    public void decreaseBalance(UUID userId, BigDecimal amount) {
        accountDao.decreaseBalance(userId, amount);
    }

}
