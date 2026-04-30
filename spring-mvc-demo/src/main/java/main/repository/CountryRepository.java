package main.repository;

import main.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findByCountryName(String countryName);
    List<Country> findByCountryNameContainingIgnoreCase(String countryName);

    @Query("""
           SELECT COUNT(c) > 0
           FROM Country c
           WHERE LOWER(c.countryName) = LOWER(:countryName)
           """)
    boolean existsCountryByName(@Param("countryName") String countryName);
}