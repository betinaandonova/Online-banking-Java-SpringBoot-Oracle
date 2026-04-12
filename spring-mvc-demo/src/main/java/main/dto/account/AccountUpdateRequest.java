package main.dto.account;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountUpdateRequest {
    private Long currencyId;
    private BigDecimal availability;
    private String iban;
}