package main.procedure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrencyTypeProcedureService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertCurrencyType(String currencyShort, String currency) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CURRENCY_INS");

        query.registerStoredProcedureParameter("p_currency_short", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency", String.class, ParameterMode.IN);

        query.setParameter("p_currency_short", currencyShort);
        query.setParameter("p_currency", currency);

        query.execute();
    }

    @Transactional
    public void updateCurrencyType(Long currencyId, String currencyShort, String currency) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CURRENCY_UPD");

        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency_short", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency", String.class, ParameterMode.IN);

        query.setParameter("p_currency_id", currencyId);
        query.setParameter("p_currency_short", currencyShort);
        query.setParameter("p_currency", currency);

        query.execute();
    }

    @Transactional
    public void deleteCurrencyType(Long currencyId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CURRENCY_DEL");

        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);

        query.setParameter("p_currency_id", currencyId);

        query.execute();
    }
}