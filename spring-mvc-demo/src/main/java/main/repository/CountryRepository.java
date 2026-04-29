package main.repository;

import main.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findByCountryName(String countryName);
    List<Country> findByCountryNameContainingIgnoreCase(String countryName);
}