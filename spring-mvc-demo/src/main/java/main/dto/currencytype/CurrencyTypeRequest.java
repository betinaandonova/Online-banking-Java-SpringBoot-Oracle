package main.dto.currencytype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyTypeRequest {
    private String currencyShort;
    private String currency;
}