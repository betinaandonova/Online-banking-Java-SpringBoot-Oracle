package main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID", nullable = false)
    private CurrencyType currency;

    @Column(name = "AVAILABILITY", nullable = false, precision = 15, scale = 2)
    private BigDecimal availability;

    @Column(name = "IBAN", nullable = false, unique = true, length = 37)
    private String iban;
}