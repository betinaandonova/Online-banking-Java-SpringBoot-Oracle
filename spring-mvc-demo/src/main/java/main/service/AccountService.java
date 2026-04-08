package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    @PersistenceContext
    private EntityManager entityManager;

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

    @Transactional
    public void deleteAccount(Long accountId) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ACCOUNT_DEL");

        query.registerStoredProcedureParameter("p_account_id", Long.class, ParameterMode.IN);
        query.setParameter("p_account_id", accountId);

        query.execute();
    }
}