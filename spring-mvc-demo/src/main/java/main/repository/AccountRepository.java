package main.repository;

import main.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByIban(String iban);

    List<Account> findByClient_Id(Long clientId);

    @Query("SELECT a FROM Account a WHERE a.client.id = :clientId")
    List<Account> findAccountsByClientId(@Param("clientId") Long clientId);

    @Query("SELECT a FROM Account a WHERE a.client.id <> :clientId")
    List<Account> findAccountsByClientIdNot(@Param("clientId") Long clientId);

    @Query("""
            SELECT a FROM Account a
            WHERE a.client.phoneNumber = :phoneNumber
            AND a.currency.id = :currencyId
            """)
    List<Account> findReceiverAccountsByPhoneAndCurrency(
            @Param("phoneNumber") String phoneNumber,
            @Param("currencyId") Long currencyId
    );
}