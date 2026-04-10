package main.repository;

import main.model.Client;
import main.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByLastNameContainingIgnoreCase(String lastName);

    List<Employee> findByPosition(String position);

    Optional <Employee> findEmployeeByPhoneNumber(String phoneNumber);
}