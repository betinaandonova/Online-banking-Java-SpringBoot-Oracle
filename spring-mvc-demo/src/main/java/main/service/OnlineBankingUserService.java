package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.dto.AccountResponse;
import main.dto.HomeUserResponse;
import main.dto.UserProfileResponse;
import main.model.Account;
import main.model.Client;
import main.model.OnlineBankingUser;
import main.repository.AccountRepository;
import main.repository.OnlineBankingUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OnlineBankingUserService implements MainReadService<OnlineBankingUser, Long> {

    private final OnlineBankingUserRepository onlineBankingUserRepository;
    private final AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public OnlineBankingUserService(OnlineBankingUserRepository onlineBankingUserRepository,
                                    AccountRepository accountRepository) {
        this.onlineBankingUserRepository = onlineBankingUserRepository;
        this.accountRepository = accountRepository;
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

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("ONLINE_USER_UPD");

        query.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_username", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_password_hash", String.class, ParameterMode.IN);

        query.setParameter("p_user_id", userId);
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

    public HomeUserResponse getHomeUserData(Long userId) {
        OnlineBankingUser user = onlineBankingUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getClient() == null) {
            throw new RuntimeException("Logged user is not linked to client");
        }

        Client client = user.getClient();

        List<AccountResponse> accounts = accountRepository.findByClient_Id(client.getId())
                .stream()
                .map(this::mapToAccountResponse)
                .toList();

        return HomeUserResponse.builder()
                .username(user.getUsername())
                .fullName(client.getName() + " " + client.getLastName())
                .egn(client.getEgn())
                .phoneNumber(client.getPhoneNumber())
                .address(client.getAddress())
                .city(client.getCity().getCityName())
                .country(client.getCity().getCountry().getCountryName())
                .accounts(accounts)
                .build();
    }

    private AccountResponse mapToAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .iban(account.getIban())
                .availability(account.getAvailability())
                .currency(account.getCurrency().getCurrencyShort())
                .build();
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

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("SPR_CLIENT_PROFILE_INFO");

        query.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

        query.setParameter("p_user_id", userId);

        query.execute();

        List<Object[]> result = query.getResultList();

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

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("SPR_EMPLOYEE_PROFILE_INFO");

        query.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

        query.setParameter("p_user_id", userId);

        query.execute();

        List<Object[]> result = query.getResultList();

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

        List<Object[]> result = query.getResultList();

        return result.stream().map(row -> {
            Client c = new Client();
            c.setId(((Number) row[0]).longValue());
            c.setName((String) row[1]);
            c.setLastName((String) row[2]);
            return c;
        }).toList();
    }
}