package main.repository;

import main.model.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurrencyTypeRepository extends JpaRepository<CurrencyType, Long> {
    Optional<CurrencyType> findById(Long id);
    Optional<CurrencyType> findCurrencyTypeByCurrencyShort(String currencyShort);
}