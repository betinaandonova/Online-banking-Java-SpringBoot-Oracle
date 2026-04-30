package main.repository;

import main.model.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CurrencyTypeRepository extends JpaRepository<CurrencyType, Long> {
    Optional<CurrencyType> findCurrencyTypeByCurrencyShort(String currencyShort);

    Optional<CurrencyType> findCurrencyTypeByCurrency(String currency);

    @Query("""
           SELECT COUNT(c) > 0
           FROM CurrencyType c
           WHERE LOWER(c.currencyShort) = LOWER(:currencyShort)
           """)
    boolean existsCurrencyByShortName(@Param("currencyShort") String currencyShort);

    @Query("""
           SELECT COUNT(c) > 0
           FROM CurrencyType c
           WHERE LOWER(c.currency) = LOWER(:currency)
           """)
    boolean existsCurrencyByName(@Param("currency") String currency);
}