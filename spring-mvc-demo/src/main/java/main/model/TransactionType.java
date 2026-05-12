package main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TRANSACTION_TYPE")
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_type_seq")
    @SequenceGenerator(
            name = "transaction_type_seq",
            sequenceName = "SEQ_TRANSACTION_TYPE_ID",
            allocationSize = 1
    )
    @Column(name = "TRANSACTION_TYPE_ID")
    private Long id;

    @Column(name = "TRANSACTION_TYPE_NAME", nullable = false, length = 20)
    private String transactionTypeName;
}