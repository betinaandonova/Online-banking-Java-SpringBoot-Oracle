package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.dto.UserProfileResponse;
import main.model.Client;
import main.model.OnlineBankingUser;
import main.repository.OnlineBankingUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OnlineBankingUserService implements MainReadService<OnlineBankingUser, Long> {

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
                                 String username,
                                 String passwordHash) {

        OnlineBankingUser existingUser = onlineBankingUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Потребителят не е намерен."));

        String finalPasswordHash = passwordHash;

        if (finalPasswordHash == null || finalPasswordHash.trim().isBlank()) {
            finalPasswordHash = existingUser.getPasswordHash();
        }

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ONLINE_USER_UPD");

        query.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_username", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_password_hash", String.class, ParameterMode.IN);

        query.setParameter("p_user_id", userId);
        query.setParameter("p_username", username);
        query.setParameter("p_password_hash", finalPasswordHash);

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

    @Override
    public List<OnlineBankingUser> findAll() {
        return onlineBankingUserRepository.findAll();
    }

    @Override
    public Optional<OnlineBankingUser> findById(Long id) {
        return onlineBankingUserRepository.findById(id);
    }

    public UserProfileResponse getClientProfile(Long userId) {

        List<Object[]> result = executeUserProfileProcedure(
                "SPR_CLIENT_PROFILE_INFO",
                userId
        );

        if (result.isEmpty()) {
            throw new RuntimeException("Client not found");
        }

        Object[] row = result.get(0);

        return UserProfileResponse.builder()
                .userType("CLIENT")
                .name((String) row[0])
                .lastName((String) row[1])
                .phoneNumber((String) row[2])
                .address((String) row[3])
                .city((String) row[4])
                .country((String) row[5])
                .build();
    }

    public UserProfileResponse getEmployeeProfile(Long userId) {

        List<Object[]> result = executeUserProfileProcedure(
                "SPR_EMPLOYEE_PROFILE_INFO",
                userId
        );

        if (result.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }

        Object[] row = result.get(0);

        return UserProfileResponse.builder()
                .userType("EMPLOYEE")
                .name((String) row[0])
                .lastName((String) row[1])
                .position((String) row[2])
                .phoneNumber((String) row[3])
                .build();
    }

    public UserProfileResponse getUserProfile(Long userId) {

        OnlineBankingUser user = onlineBankingUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEmployee() != null) {
            return getEmployeeProfile(userId);
        }

        if (user.getClient() != null) {
            return getClientProfile(userId);
        }

        throw new RuntimeException("Invalid user type");
    }

    public List<Client> getClientsWithoutUser() {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("SPR_CLIENTS_WITHOUT_USER");

        query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

        query.execute();

        @SuppressWarnings("unchecked")
        List<Object[]> result = query.getResultList();

        return result.stream().map(row -> {
            Client c = new Client();
            c.setId(((Number) row[0]).longValue());
            c.setName((String) row[1]);
            c.setLastName((String) row[2]);
            return c;
        }).toList();
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> executeUserProfileProcedure(String procedureName, Long userId) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery(procedureName);

        query.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

        query.setParameter("p_user_id", userId);

        query.execute();

        return query.getResultList();
    }
}