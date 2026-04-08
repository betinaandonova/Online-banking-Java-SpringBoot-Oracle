package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertCountry(String countryName) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("COUNTRY_INS");

        query.registerStoredProcedureParameter("p_country_name", String.class, ParameterMode.IN);

        query.setParameter("p_country_name", countryName);

        query.execute();
    }

    @Transactional
    public void updateCountry(Long countryId, String countryName) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("COUNTRY_UPD");

        query.registerStoredProcedureParameter("p_country_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_country_name", String.class, ParameterMode.IN);

        query.setParameter("p_country_id", countryId);
        query.setParameter("p_country_name", countryName);

        query.execute();
    }

    @Transactional
    public void deleteCountry(Long countryId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("COUNTRY_DEL");

        query.registerStoredProcedureParameter("p_country_id", Long.class, ParameterMode.IN);

        query.setParameter("p_country_id", countryId);

        query.execute();
    }
}