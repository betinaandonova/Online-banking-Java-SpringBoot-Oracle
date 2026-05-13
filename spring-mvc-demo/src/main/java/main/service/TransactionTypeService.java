package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.TransactionType;
import main.repository.BankTransactionRepository;
import main.repository.TransactionTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionTypeService implements MainReadService<TransactionType, Long> {

    private final TransactionTypeRepository transactionTypeRepository;
    private final BankTransactionRepository bankTransactionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TransactionTypeService(
            TransactionTypeRepository transactionTypeRepository,
            BankTransactionRepository bankTransactionRepository
    ) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.bankTransactionRepository = bankTransactionRepository;
    }

    @Transactional
    public void insertTransactionType(String transactionTypeName) {

        // Proverka dali imeto e vuvedeno
        validateTransactionTypeName(transactionTypeName);

        String cleanedName = transactionTypeName.trim();

        // Proverka dali veche sushtestvuva takuv tip tranzaktsiya
        if (transactionTypeRepository.existsByTransactionTypeNameIgnoreCase(cleanedName)) {
            throw new IllegalArgumentException("Такъв тип транзакция вече съществува.");
        }

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("TRANS_TYPE_INS");

        query.registerStoredProcedureParameter("p_type_name", String.class, ParameterMode.IN);

        query.setParameter("p_type_name", cleanedName);

        query.execute();
    }

    @Transactional
    public void updateTransactionType(Long transactionTypeId, String transactionTypeName) {

        // Proverka dali e podadeno ID
        if (transactionTypeId == null) {
            throw new IllegalArgumentException("ID на типа транзакция е задължително.");
        }

        // Proverka dali imeto e vuvedeno
        validateTransactionTypeName(transactionTypeName);

        String cleanedName = transactionTypeName.trim();

        // Proverka dali sushtestvuva zapis s tova ID
        if (!transactionTypeRepository.existsById(transactionTypeId)) {
            throw new IllegalArgumentException("Не съществува тип транзакция с това ID.");
        }

        // Proverka dali drugo ID veche ne izpolzva sushtoto ime
        if (transactionTypeRepository.existsByTransactionTypeNameIgnoreCaseAndIdNot(cleanedName, transactionTypeId)) {
            throw new IllegalArgumentException("Вече съществува друг тип транзакция със същото име.");
        }

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("TRANS_TYPE_UPD");

        query.registerStoredProcedureParameter("p_transaction_type_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_type_name", String.class, ParameterMode.IN);

        query.setParameter("p_transaction_type_id", transactionTypeId);
        query.setParameter("p_type_name", cleanedName);

        query.execute();
    }

    @Transactional
    public void deleteTransactionType(Long transactionTypeId) {

        // Proverka dali e podadeno ID
        if (transactionTypeId == null) {
            throw new IllegalArgumentException("ID на типа транзакция е задължително.");
        }

        // Proverka dali sushtestvuva zapis s tova ID
        if (!transactionTypeRepository.existsById(transactionTypeId)) {
            throw new IllegalArgumentException("Не съществува тип транзакция с това ID.");
        }

        // Proverka dali tiput tranzaktsiya se izpolzva v BANK_TRANSACTIONS
        if (bankTransactionRepository.existsByTransactionTypeId(transactionTypeId)) {
            throw new IllegalArgumentException(
                    "Този тип транзакция не може да бъде изтрит, защото се използва в банкови транзакции."
            );
        }

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("TRANS_TYPE_DEL");

        query.registerStoredProcedureParameter("p_transaction_type_id", Long.class, ParameterMode.IN);

        query.setParameter("p_transaction_type_id", transactionTypeId);

        query.execute();
    }

    // ======================
    // READ REPOSITORY
    // ======================

    public TransactionType findByTransactionTypeName(String transactionTypeName) {
        return transactionTypeRepository.findByTransactionTypeName(transactionTypeName)
                .orElseThrow(() -> new RuntimeException(
                        "Transaction type not found with name: " + transactionTypeName));
    }

    public List<TransactionType> searchByTransactionTypeName(String transactionTypeName) {
        return transactionTypeRepository.findByTransactionTypeNameContainingIgnoreCase(transactionTypeName);
    }

    @Override
    public List<TransactionType> findAll() {
        return transactionTypeRepository.findAll();
    }

    @Override
    public Optional<TransactionType> findById(Long id) {
        return transactionTypeRepository.findById(id);
    }

    // ======================
    // VALIDATIONS
    // ======================

    private void validateTransactionTypeName(String transactionTypeName) {
        if (transactionTypeName == null || transactionTypeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Името на типа транзакция е задължително.");
        }

        if (transactionTypeName.trim().length() > 20) {
            throw new IllegalArgumentException("Името на типа транзакция не може да бъде над 20 символа.");
        }
    }
}