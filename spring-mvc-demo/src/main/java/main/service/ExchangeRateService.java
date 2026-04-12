package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.Employee;
import main.model.ExchangeRate;
import main.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRateService implements MainReadService<ExchangeRate, Long>{

    private final ExchangeRateRepository exchangeRateRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

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


    @Override
    public List<ExchangeRate> findAll() {
        return exchangeRateRepository.findAll();
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
        return exchangeRateRepository.findById(id);
    }


    public List<ExchangeRate> findByRateDate(LocalDate rateDate) {
        return exchangeRateRepository.findByRateDate(rateDate);
    }

    public ExchangeRate findByCurrencyIdAndRateDate(Long currencyId, LocalDate rateDate) {
        return exchangeRateRepository.findByCurrency_IdAndRateDate(currencyId, rateDate)
                .orElseThrow(() -> new RuntimeException(
                        "Exchange rate not found for currency id " + currencyId + " and date " + rateDate));
    }

    public List<ExchangeRate> findByRateDateBetween(LocalDate startDate, LocalDate endDate) {
        return exchangeRateRepository.findByRateDateBetween(startDate, endDate);
    }
}