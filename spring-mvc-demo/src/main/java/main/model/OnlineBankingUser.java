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
@SequenceGenerator(
        name = "user_seq_generator",
        sequenceName = "SEQ_USER_ID",
        allocationSize = 1)

public class OnlineBankingUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
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