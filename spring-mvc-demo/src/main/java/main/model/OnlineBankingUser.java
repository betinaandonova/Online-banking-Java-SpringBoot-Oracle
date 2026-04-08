package main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ONLINE_BANKING_USER")

public class OnlineBankingUser {

    @Id
    @Column(name = "USER_ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "CLIENT_ID", unique = true)
    private Client client;

    @OneToOne
    @JoinColumn(name = "EMPLOYEE_ID", unique = true)
    private Employee employee;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 255)
    private String passwordHash;
}