package main.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;


@Getter
@Setter
public class AdminAccountResponse {
    private Long accountId;
    private String iban;
    private BigDecimal availability;
    private Long clientId;
    private String clientFullName;
    private String egn;
    private String phoneNumber;
    private String currencyShort;
    private String currency;

    // getters/setters
}