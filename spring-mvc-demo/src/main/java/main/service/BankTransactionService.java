package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.Client;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import main.model.BankTransaction;
import main.repository.BankTransactionRepository;
import java.util.List;
import java.util.Optional;

@Service
public class BankTransactionService implements MainReadService<BankTransaction, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionService(BankTransactionRepository bankTransactionRepository) {
        this.bankTransactionRepository = bankTransactionRepository;
    }

    // ======================
    // CRUD (PROCEDURES)
    // ======================

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

    // ======================
    // READ (REPOSITORY)
    // ======================



    public List<BankTransaction> findByAccountId(Long accountId) {
        return bankTransactionRepository.findByAccount_IdOrderByTransactionDateDesc(accountId);
    }

    public List<BankTransaction> findByTransactionTypeId(Long transactionTypeId) {
        return bankTransactionRepository.findByTransactionType_Id(transactionTypeId);
    }

    public List<BankTransaction> findByCurrencyId(Long currencyId) {
        return bankTransactionRepository.findByCurrency_Id(currencyId);
    }

    public List<BankTransaction> findByTransactionDate(LocalDate transactionDate) {
        return bankTransactionRepository.findByTransactionDate(transactionDate);
    }

    public List<BankTransaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate) {
        return bankTransactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    @Override
    public List<BankTransaction> findAll() {
        return bankTransactionRepository.findAll();
    }

    @Override
    public Optional<BankTransaction> findById(Long id) {
        return bankTransactionRepository.findById(id);
    }
}