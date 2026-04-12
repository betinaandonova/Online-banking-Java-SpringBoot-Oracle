package main.dto.exchangerate;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExchangeRateRequest {
    private Long currencyId;
    private BigDecimal rateToEur;
    private LocalDate rateDate;
}