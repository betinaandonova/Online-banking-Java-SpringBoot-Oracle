package main.procedure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityProcedureService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertCity(String cityName, Long countryId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CITY_INS");

        query.registerStoredProcedureParameter("p_city_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_country_id", Long.class, ParameterMode.IN);

        query.setParameter("p_city_name", cityName);
        query.setParameter("p_country_id", countryId);

        query.execute();
    }

    @Transactional
    public void updateCity(Long cityId, String cityName, Long countryId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CITY_UPD");

        query.registerStoredProcedureParameter("p_city_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_city_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_country_id", Long.class, ParameterMode.IN);

        query.setParameter("p_city_id", cityId);
        query.setParameter("p_city_name", cityName);
        query.setParameter("p_country_id", countryId);

        query.execute();
    }

    @Transactional
    public void deleteCity(Long cityId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CITY_DEL");

        query.registerStoredProcedureParameter("p_city_id", Long.class, ParameterMode.IN);

        query.setParameter("p_city_id", cityId);

        query.execute();
    }
}