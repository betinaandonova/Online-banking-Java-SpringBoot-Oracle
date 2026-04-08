package main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "TRANSACTION_TYPE_ID")
    private Long id;

    @Column(name = "TRANSACTION_TYPE_NAME", nullable = false, length = 20)
    private String transactionTypeName;
}