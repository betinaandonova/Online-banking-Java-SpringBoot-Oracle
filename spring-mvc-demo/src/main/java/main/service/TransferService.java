package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.Account;
import main.model.TransactionType;
import main.repository.AccountRepository;
import main.repository.TransactionTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionTypeRepository transactionTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TransferService(AccountRepository accountRepository,
                           TransactionTypeRepository transactionTypeRepository) {
        this.accountRepository = accountRepository;
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Transactional
    public void transferMoney(
            Long loggedClientId,
            String transferMethod,
            Long senderAccountId,
            String receiverPhoneNumber,
            String receiverIban,
            BigDecimal amount,
            Long currencyId
    ) {
        if (loggedClientId == null) {
            throw new RuntimeException("Невалиден потребител.");
        }

        if (transferMethod == null || transferMethod.trim().isBlank()) {
            throw new RuntimeException("Изберете начин на превод.");
        }

        if (senderAccountId == null) {
            throw new RuntimeException("Изберете сметка на подателя.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Сумата трябва да бъде по-голяма от 0.");
        }

        if (currencyId == null) {
            throw new RuntimeException("Изберете валута.");
        }

        Account senderAccount = accountRepository.findById(senderAccountId)
                .orElseThrow(() -> new RuntimeException("Сметката на подателя не е намерена."));

        if (senderAccount.getClient() == null ||
                !senderAccount.getClient().getId().equals(loggedClientId)) {
            throw new RuntimeException("Нямате право да извършвате превод от тази сметка.");
        }

        if (senderAccount.getCurrency() == null ||
                !senderAccount.getCurrency().getId().equals(currencyId)) {
            throw new RuntimeException("Избраната валута не съвпада с валутата на сметката на подателя.");
        }

        Account receiverAccount;

        if ("PHONE".equalsIgnoreCase(transferMethod)) {
            String phone = receiverPhoneNumber == null ? "" : receiverPhoneNumber.trim();

            if (phone.isBlank()) {
                throw new RuntimeException("Въведете телефонен номер на получателя.");
            }

            List<Account> foundAccounts =
                    findReceiverAccountsByPhoneAndCurrency(phone, currencyId);

            if (foundAccounts.isEmpty()) {
                throw new RuntimeException("Не е намерена сметка с този телефон и валута.");
            }

            receiverAccount = foundAccounts.get(0);

        } else if ("IBAN".equalsIgnoreCase(transferMethod)) {
            String iban = receiverIban == null ? "" : receiverIban.trim();

            if (iban.isBlank()) {
                throw new RuntimeException("Въведете IBAN на получателя.");
            }

            List<Account> foundAccounts =
                    findReceiverAccountsByIban(iban);

            if (foundAccounts.isEmpty()) {
                throw new RuntimeException("Не е намерена сметка с този IBAN.");
            }

            receiverAccount = foundAccounts.get(0);

        } else {
            throw new RuntimeException("Невалиден начин на превод.");
        }

        if (senderAccountId.equals(receiverAccount.getId())) {
            throw new RuntimeException("Подателят и получателят не могат да бъдат една и съща сметка.");
        }

        if (receiverAccount.getCurrency() == null ||
                !receiverAccount.getCurrency().getId().equals(currencyId)) {
            throw new RuntimeException("Валутата на получателя трябва да съвпада с избраната валута.");
        }

        if (senderAccount.getAvailability().compareTo(amount) < 0) {
            throw new RuntimeException("Недостатъчна наличност по сметката.");
        }

        TransactionType transferType = transactionTypeRepository.findOutgoingTransferType();

        if (transferType == null) {
            throw new RuntimeException("Липсва тип транзакция за изходящ превод.");
        }

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("TRANSFER_MONEY");

        query.registerStoredProcedureParameter("p_sender_account_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_receiver_account_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_amount", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_employee_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_transaction_type_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_receiver_phone_number", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_receiver_iban", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);

        query.setParameter("p_sender_account_id", senderAccountId);
        query.setParameter("p_receiver_account_id", receiverAccount.getId());
        query.setParameter("p_amount", amount);
        query.setParameter("p_employee_id", null);
        query.setParameter("p_transaction_type_id", transferType.getId());
        query.setParameter("p_receiver_phone_number", cleanPhoneNumber(receiverPhoneNumber));
        query.setParameter("p_receiver_iban", receiverAccount.getIban());
        query.setParameter("p_currency_id", currencyId);

        query.execute();
    }

    @Transactional(readOnly = true)
    public List<Account> findReceiverAccountsByPhoneAndCurrency(String phoneNumber, Long currencyId) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ACC_RECEIVER_BY_PHONE_AND_CURR", Account.class);

        query.registerStoredProcedureParameter("p_phone_number", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

        query.setParameter("p_phone_number", phoneNumber);
        query.setParameter("p_currency_id", currencyId);

        query.execute();

        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Account> findReceiverAccountsByIban(String iban) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ACC_RECEIVER_BY_IBAN", Account.class);

        query.registerStoredProcedureParameter("p_iban", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

        query.setParameter("p_iban", iban);

        query.execute();

        return query.getResultList();
    }

    private String cleanPhoneNumber(String receiverPhoneNumber) {
        if (receiverPhoneNumber == null || receiverPhoneNumber.trim().isBlank()) {
            return null;
        }

        return receiverPhoneNumber.trim();
    }
}