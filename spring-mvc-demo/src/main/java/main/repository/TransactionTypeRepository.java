package main.repository;

import main.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {

    Optional<TransactionType> findByTransactionTypeName(String transactionTypeName);

    List<TransactionType> findByTransactionTypeNameContainingIgnoreCase(String transactionTypeName);

}