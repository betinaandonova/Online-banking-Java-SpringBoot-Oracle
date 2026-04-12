package main.dto.banktransaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BankTransactionCreateRequest {
    private Long employeeId;
    private Long transactionTypeId;
    private BigDecimal amount;
    private Long accountId;
    private String receiverPhoneNumber;
    private String receiverIban;
    private Long currencyId;
    private LocalDate transactionDate;
}