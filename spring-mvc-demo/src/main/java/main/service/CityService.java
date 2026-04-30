package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.exception.InvalidDataException;
import main.model.City;
import main.repository.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CityService implements MainReadService<City, Long>{

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

        String trimmedCityName = cityName == null ? "" : cityName.trim();

        if (trimmedCityName.isBlank()) {
            throw new InvalidDataException("Името на града е задължително.");
        }

        if (countryId == null) {
            throw new InvalidDataException("Трябва да бъде избрана държава.");
        }

        if (cityRepository.existsCityByNameAndCountry(trimmedCityName, countryId)) {
            throw new InvalidDataException("Вече съществува град с това име в избраната държава.");
        }

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CITY_INS");

        query.registerStoredProcedureParameter("p_city_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_country_id", Long.class, ParameterMode.IN);

        query.setParameter("p_city_name", trimmedCityName);
        query.setParameter("p_country_id", countryId);

        query.execute();
    }

    @Transactional
    public void updateCity(Long cityId, String cityName, Long countryId) {

        String trimmedCityName = cityName == null ? "" : cityName.trim();

        if (cityId == null) {
            throw new InvalidDataException("Невалиден град.");
        }

        if (trimmedCityName.isBlank()) {
            throw new InvalidDataException("Името на града е задължително.");
        }

        if (countryId == null) {
            throw new InvalidDataException("Трябва да бъде избрана държава.");
        }

        if (cityRepository.existsOtherCityByNameAndCountry(trimmedCityName, countryId, cityId)) {
            throw new InvalidDataException("Вече съществува друг град с това име в избраната държава.");
        }

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CITY_UPD");

        query.registerStoredProcedureParameter("p_city_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_city_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_country_id", Long.class, ParameterMode.IN);

        query.setParameter("p_city_id", cityId);
        query.setParameter("p_city_name", trimmedCityName);
        query.setParameter("p_country_id", countryId);

        query.execute();
    }

    @Transactional
    public void deleteCity(Long cityId)
    {

        if (cityId == null)
        {
            throw new InvalidDataException("Невалиден град.");
        }

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CITY_DEL");

        query.registerStoredProcedureParameter("p_city_id", Long.class, ParameterMode.IN);
        query.setParameter("p_city_id", cityId);

        query.execute();
    }

    // ======================
    // READ (REPOSITORY)
    // ======================

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public Optional<City> findById(Long id) {
        return cityRepository.findById(id);
    }
}