package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.City;
import main.repository.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    // ======================
    // CRUD (PROCEDURES)
    // ======================

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

    // ======================
    // READ (REPOSITORY)
    // ======================

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public City findById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found with id: " + id));
    }

    public List<City> findByName(String name)
    {
        return cityRepository.findByCityNameContainingIgnoreCase(name);
    }

    public List<City> findByCountryId(Long countryId) {
        return cityRepository.findByCountry_Id(countryId);
    }
}