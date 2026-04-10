package main.repository;

import main.model.Client;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByCity_Id(Long cityId);

    List<Client> findByNameContainingIgnoreCase(String name);

    List<Client> findByLastNameContainingIgnoreCase(String lastName);

    Optional<Client> findByEgn(String egn);
}