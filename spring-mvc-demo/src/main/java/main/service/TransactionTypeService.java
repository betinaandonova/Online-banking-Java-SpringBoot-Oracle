package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.OnlineBankingUser;
import main.model.TransactionType;
import main.repository.TransactionTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionTypeService implements MainReadService<TransactionType, Long>{

    private final TransactionTypeRepository transactionTypeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Transactional
    public void insertTransactionType(String transactionTypeName) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("TRANSACTION_TYPE_INS");

        query.registerStoredProcedureParameter("p_transaction_type_name", String.class, ParameterMode.IN);

        query.setParameter("p_transaction_type_name", transactionTypeName);

        query.execute();
    }

    @Transactional
    public void updateTransactionType(Long transactionTypeId, String transactionTypeName) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("TRANSACTION_TYPE_UPD");

        query.registerStoredProcedureParameter("p_transaction_type_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_transaction_type_name", String.class, ParameterMode.IN);

        query.setParameter("p_transaction_type_id", transactionTypeId);
        query.setParameter("p_transaction_type_name", transactionTypeName);

        query.execute();
    }

    @Transactional
    public void deleteTransactionType(Long transactionTypeId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("TRANSACTION_TYPE_DEL");

        query.registerStoredProcedureParameter("p_transaction_type_id", Long.class, ParameterMode.IN);

        query.setParameter("p_transaction_type_id", transactionTypeId);

        query.execute();
    }

    // ======================
    // READ (REPOSITORY)
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
}