package main.repository;

import main.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByCity_Id(Long cityId);

    List<Client> findByNameContainingIgnoreCase(String name);

    List<Client> findByLastNameContainingIgnoreCase(String lastName);

    Optional<Client> findByEgn(String egn);

    @Query("SELECT COUNT(c) > 0 FROM Client c WHERE c.egn = :egn")
    boolean existsClientByEgn(@Param("egn") String egn);

    @Query("SELECT COUNT(c) > 0 FROM Client c WHERE c.phoneNumber = :phoneNumber")
    boolean existsClientByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}