package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.Employee;
import main.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void insertEmployee(String name,
                               String lastName,
                               String position,
                               String phoneNumber) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("EMPLOYEE_INS");

        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_last_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_position", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_phone_number", String.class, ParameterMode.IN);

        query.setParameter("p_name", name);
        query.setParameter("p_last_name", lastName);
        query.setParameter("p_position", position);
        query.setParameter("p_phone_number", phoneNumber);

        query.execute();
    }

    @Transactional
    public void updateEmployee(Long employeeId,
                               String name,
                               String lastName,
                               String position,
                               String phoneNumber) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("EMPLOYEE_UPD");

        query.registerStoredProcedureParameter("p_employee_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_last_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_position", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_phone_number", String.class, ParameterMode.IN);

        query.setParameter("p_employee_id", employeeId);
        query.setParameter("p_name", name);
        query.setParameter("p_last_name", lastName);
        query.setParameter("p_position", position);
        query.setParameter("p_phone_number", phoneNumber);

        query.execute();
    }

    @Transactional
    public void deleteEmployee(Long employeeId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("EMPLOYEE_DEL");

        query.registerStoredProcedureParameter("p_employee_id", Long.class, ParameterMode.IN);

        query.setParameter("p_employee_id", employeeId);

        query.execute();
    }

    // ======================
    // READ (REPOSITORY)
    // ======================

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    public List<Employee> findByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Employee> findByLastName(String lastName) {
        return employeeRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    public List<Employee> findByPosition(String position) {
        return employeeRepository.findByPosition(position);
    }

    public Employee findByPhoneNumber(String phoneNumber) {
        return employeeRepository.findEmployeeByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Employee not found with phone number: " + phoneNumber));
    }
}