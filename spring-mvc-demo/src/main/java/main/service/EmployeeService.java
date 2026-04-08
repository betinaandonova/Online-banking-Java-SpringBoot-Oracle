package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    @PersistenceContext
    private EntityManager entityManager;

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
}