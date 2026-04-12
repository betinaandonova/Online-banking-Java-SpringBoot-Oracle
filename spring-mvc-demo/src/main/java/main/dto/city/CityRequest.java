package main.dto.city;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityRequest {
    private String cityName;
    private Long countryId;
}