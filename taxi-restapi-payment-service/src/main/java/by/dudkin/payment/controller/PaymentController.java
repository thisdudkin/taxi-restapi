package by.dudkin.payment.controller;

import by.dudkin.common.util.TransactionRequest;
import by.dudkin.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(PaymentController.URI)
public class PaymentController {
    public static final String URI = "/api/payments";
    private final TransactionService transactionService;

    @PutMapping
    public ResponseEntity<Void> handleTransaction(@RequestBody TransactionRequest<UUID> transactionRequest) throws SQLException {
        transactionService.handleTransaction(transactionRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
