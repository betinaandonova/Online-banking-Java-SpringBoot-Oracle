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
public class ExchangeRateService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertExchangeRate(Long currencyId,
                                   BigDecimal rateToEur,
                                   LocalDate rateDate) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("EXCHANGE_RATE_INS");

        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_rate_to_eur", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_rate_date", LocalDate.class, ParameterMode.IN);

        query.setParameter("p_currency_id", currencyId);
        query.setParameter("p_rate_to_eur", rateToEur);
        query.setParameter("p_rate_date", rateDate);

        query.execute();
    }

    @Transactional
    public void updateExchangeRate(Long rateId,
                                   Long currencyId,
                                   BigDecimal rateToEur,
                                   LocalDate rateDate) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("EXCHANGE_RATE_UPD");

        query.registerStoredProcedureParameter("p_rate_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_rate_to_eur", BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_rate_date", LocalDate.class, ParameterMode.IN);

        query.setParameter("p_rate_id", rateId);
        query.setParameter("p_currency_id", currencyId);
        query.setParameter("p_rate_to_eur", rateToEur);
        query.setParameter("p_rate_date", rateDate);

        query.execute();
    }

    @Transactional
    public void deleteExchangeRate(Long rateId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("EXCHANGE_RATE_DEL");

        query.registerStoredProcedureParameter("p_rate_id", Long.class, ParameterMode.IN);
        query.setParameter("p_rate_id", rateId);

        query.execute();
    }

    // ======================
    // READ (REPOSITORY)
    // ======================

}