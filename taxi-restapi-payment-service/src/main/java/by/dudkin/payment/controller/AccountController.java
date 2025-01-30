package by.dudkin.payment.controller;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.TransactionRequest;
import by.dudkin.payment.dto.AccountResponse;
import by.dudkin.payment.service.AccountService;
import by.dudkin.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AccountController.URI)
public class AccountController {
    public static final String URI = "/api/accounts";
    private final AccountService accountService;

    @GetMapping("/{userId}")
    public ResponseEntity<BalanceResponse<UUID>> getBalance(@PathVariable UUID userId) throws SQLException {
        return ResponseEntity.status(200).body(accountService.getBalance(userId));
    }

}
