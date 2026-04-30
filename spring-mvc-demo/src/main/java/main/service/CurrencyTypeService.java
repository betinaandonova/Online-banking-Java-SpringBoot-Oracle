package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.exception.InvalidDataException;
import main.model.CurrencyType;
import main.repository.CurrencyTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyTypeService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CurrencyTypeRepository currencyRepository;

    public CurrencyTypeService(CurrencyTypeRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<CurrencyType> findAll() {
        return currencyRepository.findAll();
    }

    @Transactional
    public void insertCurrency(String currencyShort, String currency) {

        String trimmedCurrencyShort = currencyShort == null ? "" : currencyShort.trim().toUpperCase();
        String trimmedCurrency = currency == null ? "" : currency.trim();

        if (trimmedCurrencyShort.isBlank()) {
            throw new InvalidDataException("Краткото име на валутата е задължително.");
        }

        if (trimmedCurrency.isBlank()) {
            throw new InvalidDataException("Името на валутата е задължително.");
        }

        if (currencyRepository.existsCurrencyByShortName(trimmedCurrencyShort)) {
            throw new InvalidDataException("Вече съществува валута с това кратко име.");
        }

        if (currencyRepository.existsCurrencyByName(trimmedCurrency)) {
            throw new InvalidDataException("Вече съществува валута с това име.");
        }

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CURRENCY_INS");

        query.registerStoredProcedureParameter("p_currency_short", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency", String.class, ParameterMode.IN);

        query.setParameter("p_currency_short", trimmedCurrencyShort);
        query.setParameter("p_currency", trimmedCurrency);

        query.execute();
    }

    @Transactional
    public void deleteCurrency(Long id) {

        if (id == null) {
            throw new InvalidDataException("Невалидна валута.");
        }

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CURRENCY_DEL");

        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.setParameter("p_currency_id", id);

        query.execute();
    }
}