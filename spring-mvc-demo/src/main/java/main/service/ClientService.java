package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import main.exception.InvalidDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import main.model.Client;
import main.repository.ClientRepository;
import java.util.List;
import java.util.Optional;
import main.dto.AdminClientResponse;
import java.util.ArrayList;

@Service
public class ClientService implements MainReadService<Client, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void insertClient(String name,
                             String lastName,
                             String egn,
                             String phoneNumber,
                             String address,
                             Long cityId) {

        String trimmedName = name.trim();
        String trimmedLastName = lastName.trim();
        String trimmedEgn = egn.trim();
        String trimmedPhoneNumber = phoneNumber.trim();
        String trimmedAddress = address.trim();

        if (trimmedName.isBlank()
                || trimmedLastName.isBlank()
                || trimmedEgn.isBlank()
                || trimmedPhoneNumber.isBlank()
                || trimmedAddress.isBlank()) {
            throw new InvalidDataException("Всички полета са задължителни.");
        }

        if (clientRepository.existsClientByEgn(trimmedEgn)) {
            throw new InvalidDataException("Вече съществува клиент с това ЕГН.");
        }

        if (clientRepository.existsClientByPhoneNumber(trimmedPhoneNumber)) {
            throw new InvalidDataException("Вече съществува клиент с този телефонен номер.");
        }

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CLIENT_INS");

        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_last_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_egn", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_phone", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_address", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_city_id", Long.class, ParameterMode.IN);

        query.setParameter("p_name", trimmedName);
        query.setParameter("p_last_name", trimmedLastName);
        query.setParameter("p_egn", trimmedEgn);
        query.setParameter("p_phone", trimmedPhoneNumber);
        query.setParameter("p_address", trimmedAddress);
        query.setParameter("p_city_id", cityId);

        query.execute();
    }

    @Transactional
    public void updateClient(Long clientId,
                             String name,
                             String lastName,
                             String phoneNumber,
                             String address,
                             Long cityId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CLIENT_UPD");

        query.registerStoredProcedureParameter("p_client_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_last_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_phone", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_address", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_city_id", Long.class, ParameterMode.IN);

        query.setParameter("p_client_id", clientId);
        query.setParameter("p_name", name);
        query.setParameter("p_last_name", lastName);
        query.setParameter("p_phone", phoneNumber);
        query.setParameter("p_address", address);
        query.setParameter("p_city_id", cityId);

        query.execute();
    }

    @Transactional
    public void deleteClient(Long clientId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CLIENT_DEL");

        query.registerStoredProcedureParameter("p_client_id", Long.class, ParameterMode.IN);
        query.setParameter("p_client_id", clientId);

        query.execute();
    }

    // ======================
    // READ (REPOSITORY)
    // ======================

    @Override
    public List<Client> findAll()
    {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> findById(Long id)
    {
        return clientRepository.findById(id);
    }



    public List<AdminClientResponse> getAdminClients() {

        StoredProcedureQuery query =
                entityManager.createStoredProcedureQuery("SPR_ADMIN_CLIENTS");

        query.registerStoredProcedureParameter(
                "p_result",
                void.class,
                ParameterMode.REF_CURSOR
        );

        query.execute();

        List<Object[]> rows = query.getResultList();
        List<AdminClientResponse> clients = new ArrayList<>();

        for (Object[] row : rows) {
            clients.add(new AdminClientResponse(
                    ((Number) row[0]).longValue(),
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    (String) row[4],
                    (String) row[5],
                    (String) row[6],
                    (String) row[7]
            ));
        }

        return clients;
    }
}