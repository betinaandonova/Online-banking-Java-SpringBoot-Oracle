package main.repository;

import main.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {

    Optional<TransactionType> findByTransactionTypeName(String transactionTypeName);

    List<TransactionType> findByTransactionTypeNameContainingIgnoreCase(String transactionTypeName);

    // Proverka dali sushtestvuva tip tranzaktsiya s takova ime
    boolean existsByTransactionTypeNameIgnoreCase(String transactionTypeName);

    // Proverka dali ima drug zapis sus sushtoto ime, bez tekushtoto ID
    boolean existsByTransactionTypeNameIgnoreCaseAndIdNot(String transactionTypeName, Long id);

    Optional<TransactionType> findFirstByTransactionTypeNameContainingIgnoreCase(String transactionTypeName);

    @Query(value = """
            SELECT *
            FROM TRANSACTION_TYPE
            WHERE UPPER(TRANSACTION_TYPE_NAME) LIKE UPPER('%Изх%')
            AND ROWNUM = 1
            """, nativeQuery = true)
    TransactionType findOutgoingTransferType();
}