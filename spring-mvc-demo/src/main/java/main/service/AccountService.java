package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.Account;
import main.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import main.dto.AdminAccountResponse;
import main.enums.AccountSearchType;
import java.util.ArrayList;

@Service
public class AccountService implements MainReadService<Account, Long>{

    @PersistenceContext
    private EntityManager entityManager;

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // ======================
    // CRUD (PROCEDURES)
    // ======================

    @Transactional
    public void insertAccount(Long clientId,
                              Long currencyId,
                              BigDecimal availability,
                              String iban) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ACCOUNT_INS");

        query.registerStoredProcedureParameter("p_client_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_availability", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_iban", String.class, ParameterMode.IN);

        query.setParameter("p_client_id", clientId);
        query.setParameter("p_currency_id", currencyId);
        query.setParameter("p_availability", availability);
        query.setParameter("p_iban", iban);

        query.execute();
    }

    @Transactional
    public void updateAccount(Long accountId,
                              Long currencyId,
                              BigDecimal availability,
                              String iban) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ACCOUNT_UPD");

        query.registerStoredProcedureParameter("p_account_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_availability", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_iban", String.class, ParameterMode.IN);

        query.setParameter("p_account_id", accountId);
        query.setParameter("p_currency_id", currencyId);
        query.setParameter("p_availability", availability);
        query.setParameter("p_iban", iban);

        query.execute();
    }


    // ======================
    // READ (REPOSITORY)
    // ======================



    public List<Account> findByClientId(Long clientId) {
        return accountRepository.findByClient_Id(clientId);
    }

    public Account findByIban(String iban) {
        return accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found with IBAN: " + iban));
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public List<AdminAccountResponse> searchAccounts(AccountSearchType searchType, String searchValue) {

        if (searchType == null || searchValue == null || searchValue.isBlank()) {
            return mapAccountsToDto(accountRepository.findAll());
        }

        return switch (searchType) {
            case CLIENT_ID -> callProcedure("ACC_SEARCH_BY_CLIENT_ID", "p_client_id", Long.valueOf(searchValue));
            case FIRST_NAME -> callProcedure("ACC_SEARCH_BY_FIRST_NAME", "p_first_name", searchValue);
            case LAST_NAME -> callProcedure("ACC_SEARCH_BY_LAST_NAME", "p_last_name", searchValue);
            case EGN -> callProcedure("ACC_SEARCH_BY_EGN", "p_egn", searchValue);
            case PHONE -> callProcedure("ACC_SEARCH_BY_PHONE", "p_phone", searchValue);
        };
    }

    private List<AdminAccountResponse> callProcedure(String procedureName,
                                                     String paramName,
                                                     Object paramValue) {

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(procedureName);

        if (paramValue instanceof Long) {
            query.registerStoredProcedureParameter(paramName, Long.class, ParameterMode.IN);
        } else {
            query.registerStoredProcedureParameter(paramName, String.class, ParameterMode.IN);
        }

        query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

        query.setParameter(paramName, paramValue);

        query.execute();

        List<Object[]> rows = query.getResultList();
        List<AdminAccountResponse> result = new ArrayList<>();

        for (Object[] row : rows) {
            result.add(mapRowToDto(row));
        }

        return result;
    }

    private AdminAccountResponse mapRowToDto(Object[] row) {

        AdminAccountResponse dto = new AdminAccountResponse();

        dto.setAccountId(((Number) row[0]).longValue());
        dto.setIban((String) row[1]);
        dto.setAvailability((BigDecimal) row[2]);

        dto.setClientId(((Number) row[3]).longValue());

        String firstName = (String) row[4];
        String lastName = (String) row[5];
        dto.setClientFullName(firstName + " " + lastName);

        dto.setEgn((String) row[6]);
        dto.setPhoneNumber((String) row[7]);

        dto.setCurrencyShort((String) row[8]);
        dto.setCurrency((String) row[9]);

        return dto;
    }

    private List<AdminAccountResponse> mapAccountsToDto(List<Account> accounts) {

        return accounts.stream().map(account -> {
            AdminAccountResponse dto = new AdminAccountResponse();

            dto.setAccountId(account.getId());
            dto.setIban(account.getIban());
            dto.setAvailability(account.getAvailability());

            dto.setClientId(account.getClient().getId());
            dto.setClientFullName(
                    account.getClient().getName() + " " + account.getClient().getLastName()
            );

            dto.setEgn(account.getClient().getEgn());
            dto.setPhoneNumber(account.getClient().getPhoneNumber());

            dto.setCurrencyShort(account.getCurrency().getCurrencyShort());
            dto.setCurrency(account.getCurrency().getCurrency());

            return dto;
        }).toList();
    }

    @Transactional
    public void updateAccountAvailability(Long accountId, BigDecimal availability) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ACCOUNT_AVAILABILITY_UPD");

        query.registerStoredProcedureParameter("p_account_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_availability", BigDecimal.class, ParameterMode.IN);

        query.setParameter("p_account_id", accountId);
        query.setParameter("p_availability", availability);

        query.execute();
    }

    @Transactional
    public void deleteAccount(Long accountId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ACCOUNT_DEL");

        query.registerStoredProcedureParameter("p_account_id", Long.class, ParameterMode.IN);
        query.setParameter("p_account_id", accountId);

        query.execute();
    }
}