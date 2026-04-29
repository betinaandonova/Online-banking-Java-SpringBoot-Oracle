package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.Country;
import main.model.Employee;
import main.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService implements MainReadService<Country, Long> {

    private final CountryRepository countryRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional
    public void insertCountry(String countryName) {

        // ✅ ВАЛИДАЦИЯ
        if (countryName == null || countryName.trim().isEmpty()) {
            throw new RuntimeException("Country name cannot be empty");
        }

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("COUNTRY_INS");

        query.registerStoredProcedureParameter("p_country_name", String.class, ParameterMode.IN);

        query.setParameter("p_country_name", countryName);

        query.execute();
    }

    @Transactional
    public void updateCountry(Long countryId, String countryName)
    {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("COUNTRY_UPD");

        query.registerStoredProcedureParameter("p_country_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_country_name", String.class, ParameterMode.IN);

        query.setParameter("p_country_id", countryId);
        query.setParameter("p_country_name", countryName);

        query.execute();
    }

    @Transactional
    public void deleteCountry(Long countryId)
    {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("COUNTRY_DEL");

        query.registerStoredProcedureParameter("p_country_id", Long.class, ParameterMode.IN);

        query.setParameter("p_country_id", countryId);

        query.execute();
    }

    // ======================
    // READ (REPOSITORY)
    // ======================

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> findById(Long id) {
        return countryRepository.findById(id);
    }

    public List<Country> findByCountryName(String countryName){
        return countryRepository.findByCountryName(countryName);
    }
    public List<Country> getCountries(String searchValue) {

        if (searchValue == null || searchValue.trim().isEmpty()) {
            return findAll();
        }

        return countryRepository.findByCountryNameContainingIgnoreCase(searchValue);
    }
}