package main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CLIENT")
public class Client {

    @Id
    @Column(name = "CLIENT_ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    @Column(name = "LAST_NAME", nullable = false, length = 20)
    private String lastName;

    @Column(name = "EGN", nullable = false, length = 10)
    private String egn;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "ADDRESS", nullable = false, length = 100)
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CITY_ID", nullable = false)
    private City city;
}