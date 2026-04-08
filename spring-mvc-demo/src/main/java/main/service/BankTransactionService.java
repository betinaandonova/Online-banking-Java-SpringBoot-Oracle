package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BankTransactionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertTransaction(Long employeeId,
                                  Long transactionTypeId,
                                  BigDecimal amount,
                                  Long accountId,
                                  String receiverPhoneNumber,
                                  String receiverIban,
                                  Long currencyId,
                                  LocalDate transactionDate) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("BANK_TRANS_INS");

        query.registerStoredProcedureParameter("p_employee_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_transaction_type_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_amount", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_account_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_receiver_phone_number", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_receiver_iban", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_transaction_date", LocalDate.class, ParameterMode.IN);

        query.setParameter("p_employee_id", employeeId);
        query.setParameter("p_transaction_type_id", transactionTypeId);
        query.setParameter("p_amount", amount);
        query.setParameter("p_account_id", accountId);
        query.setParameter("p_receiver_phone_number", receiverPhoneNumber);
        query.setParameter("p_receiver_iban", receiverIban);
        query.setParameter("p_currency_id", currencyId);
        query.setParameter("p_transaction_date", transactionDate);

        query.execute();
    }

    @Transactional
    public void updateTransaction(Long bankTransactionId,
                                  Long employeeId,
                                  Long transactionTypeId,
                                  BigDecimal amount,
                                  Long accountId,
                                  String receiverPhoneNumber,
                                  String receiverIban,
                                  Long currencyId,
                                  LocalDate transactionDate) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("BANK_TRANS_UPD");

        query.registerStoredProcedureParameter("p_bank_transaction_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_employee_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_transaction_type_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_amount", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_account_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_receiver_phone_number", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_receiver_iban", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_transaction_date", LocalDate.class, ParameterMode.IN);

        query.setParameter("p_bank_transaction_id", bankTransactionId);
        query.setParameter("p_employee_id", employeeId);
        query.setParameter("p_transaction_type_id", transactionTypeId);
        query.setParameter("p_amount", amount);
        query.setParameter("p_account_id", accountId);
        query.setParameter("p_receiver_phone_number", receiverPhoneNumber);
        query.setParameter("p_receiver_iban", receiverIban);
        query.setParameter("p_currency_id", currencyId);
        query.setParameter("p_transaction_date", transactionDate);

        query.execute();
    }

    @Transactional
    public void deleteTransaction(Long bankTransactionId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("BANK_TRANS_DEL");

        query.registerStoredProcedureParameter("p_bank_transaction_id", Long.class, ParameterMode.IN);
        query.setParameter("p_bank_transaction_id", bankTransactionId);

        query.execute();
    }
}