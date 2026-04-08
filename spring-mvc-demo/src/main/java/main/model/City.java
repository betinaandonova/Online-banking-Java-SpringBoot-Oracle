package main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CITY")
public class City {

    @Id
    @Column(name = "CITY_ID")
    private Long id;

    @Column(name = "CITY_NAME", nullable = false, length = 50)
    private String cityName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COUNTRY_ID", nullable = false)
    private Country country;
}