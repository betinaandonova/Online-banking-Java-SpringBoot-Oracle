package main.repository;

import main.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CityRepository extends JpaRepository<City, Long> {

    @Query("""
           SELECT COUNT(c) > 0
           FROM City c
           WHERE LOWER(c.cityName) = LOWER(:cityName)
             AND c.country.id = :countryId
           """)
    boolean existsCityByNameAndCountry(@Param("cityName") String cityName,
                                       @Param("countryId") Long countryId);

    @Query("""
           SELECT COUNT(c) > 0
           FROM City c
           WHERE LOWER(c.cityName) = LOWER(:cityName)
             AND c.country.id = :countryId
             AND c.id <> :cityId
           """)
    boolean existsOtherCityByNameAndCountry(@Param("cityName") String cityName,
                                            @Param("countryId") Long countryId,
                                            @Param("cityId") Long cityId);
}