package main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CURRENCY_TYPE")
public class CurrencyType {

    @Id
    @Column(name = "CURRENCY_ID")
    private Long id;

    @Column(name = "CURRENCY_SHORT", nullable = false, length = 5)
    private String currencyShort;

    @Column(name = "CURRENCY", nullable = false, length = 50)
    private String currency;
}