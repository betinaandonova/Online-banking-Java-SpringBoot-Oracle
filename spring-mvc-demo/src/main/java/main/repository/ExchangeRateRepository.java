package main.repository;

import main.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> findByCurrency_Id(Long currencyId);

    List<ExchangeRate> findByRateDate(LocalDate rateDate);

    Optional<ExchangeRate> findByCurrency_IdAndRateDate(Long currencyId, LocalDate rateDate);

    List<ExchangeRate> findByRateDateBetween(LocalDate startDate, LocalDate endDate);
}