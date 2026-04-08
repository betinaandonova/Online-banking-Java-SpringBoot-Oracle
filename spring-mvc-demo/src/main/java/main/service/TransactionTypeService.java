package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionTypeService {

    @PersistenceContext
    private EntityManager entityManager;

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
}