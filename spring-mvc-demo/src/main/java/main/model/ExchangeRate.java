package main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "EXCHANGE_RATE")
public class ExchangeRate {

    @Id
    @Column(name = "RATE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID", nullable = false)
    private CurrencyType currency;

    @Column(name = "RATE_TO_EUR", nullable = false, precision = 15, scale = 6)
    private BigDecimal rateToEur;

    @Column(name = "RATE_DATE")
    private LocalDate rateDate;
}