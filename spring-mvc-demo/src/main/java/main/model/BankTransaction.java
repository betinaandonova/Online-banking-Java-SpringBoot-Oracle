package main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "BANK_TRANSACTIONS")
public class BankTransaction {

    @Id
    @Column(name = "BANK_TRANSACTION_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "TRANSACTION_TYPE_ID", nullable = false)
    private TransactionType transactionType;

    @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account account;

    @Column(name = "RECEIVER_PHONE_NUMBER", length = 15)
    private String receiverPhoneNumber;

    @Column(name = "RECEIVER_IBAN", length = 37)
    private String receiverIban;

    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID", nullable = false)
    private CurrencyType currency;

    @Column(name = "TRANSACTION_DATE", nullable = false)
    private LocalDate transactionDate;
}