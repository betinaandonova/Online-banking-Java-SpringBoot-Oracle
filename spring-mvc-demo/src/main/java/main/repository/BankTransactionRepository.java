package main.repository;

import main.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findByAccount_IdOrderByTransactionDateDesc(Long accountId);

    List<BankTransaction> findByTransactionType_Id(Long transactionTypeId);

    List<BankTransaction> findByCurrency_Id(Long currencyId);

    List<BankTransaction> findByTransactionDate(LocalDate transactionDate);

    List<BankTransaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

    Long Id(Long id);

    Long id(Long id);
}