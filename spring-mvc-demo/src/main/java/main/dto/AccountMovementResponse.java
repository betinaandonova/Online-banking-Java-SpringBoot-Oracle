package main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountMovementResponse {

    private LocalDateTime movementDate;

    private String counterparty;

    private BigDecimal amount;

    private String currencyShort;

    private String direction;

    private BigDecimal signedAmount;
}