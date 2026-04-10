package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.model.OnlineBankingUser;
import main.repository.OnlineBankingUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OnlineBankingUserService {

    private final OnlineBankingUserRepository onlineBankingUserRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public OnlineBankingUserService(OnlineBankingUserRepository onlineBankingUserRepository) {
        this.onlineBankingUserRepository = onlineBankingUserRepository;
    }

    @Transactional
    public void insertOnlineUser(Long clientId,
                                 Long employeeId,
                                 String username,
                                 String passwordHash) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ONLINE_USER_INS");

        query.registerStoredProcedureParameter("p_client_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_employee_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_username", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_password_hash", String.class, ParameterMode.IN);

        query.setParameter("p_client_id", clientId);
        query.setParameter("p_employee_id", employeeId);
        query.setParameter("p_username", username);
        query.setParameter("p_password_hash", passwordHash);

        query.execute();
    }

    @Transactional
    public void updateOnlineUser(Long userId,
                                 Long clientId,
                                 Long employeeId,
                                 String username,
                                 String passwordHash) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ONLINE_USER_UPD");

        query.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_client_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_employee_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_username", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_password_hash", String.class, ParameterMode.IN);

        query.setParameter("p_user_id", userId);
        query.setParameter("p_client_id", clientId);
        query.setParameter("p_employee_id", employeeId);
        query.setParameter("p_username", username);
        query.setParameter("p_password_hash", passwordHash);

        query.execute();
    }

    @Transactional
    public void deleteOnlineUser(Long userId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ONLINE_USER_DEL");

        query.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN);
        query.setParameter("p_user_id", userId);

        query.execute();
    }

    @Transactional
    public void createClientUser(Long clientId,
                                 String username,
                                 String passwordHash) {
        insertOnlineUser(clientId, null, username, passwordHash);
    }

    @Transactional
    public void createEmployeeUser(Long employeeId,
                                   String username,
                                   String passwordHash) {
        insertOnlineUser(null, employeeId, username, passwordHash);
    }

    // ======================
    // READ (REPOSITORY)
    // ======================

    public List<OnlineBankingUser> findAll() {
        return onlineBankingUserRepository.findAll();
    }

    public OnlineBankingUser findById(Long id) {
        return onlineBankingUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public OnlineBankingUser findByUsername(String username) {
        return onlineBankingUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public OnlineBankingUser findByClientId(Long clientId) {
        return onlineBankingUserRepository.findByClient_Id(clientId)
                .orElseThrow(() -> new RuntimeException("User not found for client id: " + clientId));
    }

    public OnlineBankingUser findByEmployeeId(Long employeeId) {
        return onlineBankingUserRepository.findByEmployee_Id(employeeId)
                .orElseThrow(() -> new RuntimeException("User not found for employee id: " + employeeId));
    }
}