package main.repository;

import main.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByCity_Id(Long cityId);

    List<Client> findByNameContainingIgnoreCase(String name);

    List<Client> findByLastNameContainingIgnoreCase(String lastName);

    List<Client> findByEgn(String egn);
}