package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.City;
import main.model.Country;
import main.model.CurrencyType;
import main.repository.CurrencyTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyTypeService implements MainReadService<CurrencyType, Long>{

    private final CurrencyTypeRepository currencyTypeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public CurrencyTypeService(CurrencyTypeRepository currencyTypeRepository)
    {
        this.currencyTypeRepository = currencyTypeRepository;
    }

    @Transactional
    public void insertCurrencyType(String currencyShort, String currency)
    {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CURRENCY_INS");

        query.registerStoredProcedureParameter("p_currency_short", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency", String.class, ParameterMode.IN);

        query.setParameter("p_currency_short", currencyShort);
        query.setParameter("p_currency", currency);

        query.execute();
    }

    @Transactional
    public void updateCurrencyType(Long currencyId, String currencyShort, String currency)
    {

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
    public void deleteCurrencyType(Long currencyId)
    {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CURRENCY_DEL");

        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);

        query.setParameter("p_currency_id", currencyId);

        query.execute();
    }

    // ======================
    // READ (REPOSITORY)
    // ======================


    public CurrencyType findByShort(String currencyShort){
        return currencyTypeRepository.findCurrencyTypeByCurrencyShort(currencyShort)
                .orElseThrow(() ->new RuntimeException("This currency doesn't exist: " + currencyShort));
    }

    public CurrencyType findCurrencyTypeByCurrency(String currency){
        return currencyTypeRepository.findCurrencyTypeByCurrency(currency)
                .orElseThrow(() ->new RuntimeException("This currency doesn't exist: " + currency));
    }

    @Override
    public List<CurrencyType> findAll() {
        return currencyTypeRepository.findAll();
    }

    @Override
    public Optional<CurrencyType> findById(Long id) {
        return currencyTypeRepository.findById(id);
    }
}