package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import main.model.Client;
import main.repository.ClientRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

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

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CLIENT_INS");

        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_last_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_egn", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_phone", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_address", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_city_id", Long.class, ParameterMode.IN);

        query.setParameter("p_name", name);
        query.setParameter("p_last_name", lastName);
        query.setParameter("p_egn", egn);
        query.setParameter("p_phone", phoneNumber);
        query.setParameter("p_address", address);
        query.setParameter("p_city_id", cityId);

        query.execute();
    }

    @Transactional
    public void updateClient(Long clientId,
                             String name,
                             String lastName,
                             String egn,
                             String phoneNumber,
                             String address,
                             Long cityId) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("CLIENT_UPD");

        query.registerStoredProcedureParameter("p_client_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_last_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_egn", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_phone", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_address", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_city_id", Long.class, ParameterMode.IN);

        query.setParameter("p_client_id", clientId);
        query.setParameter("p_name", name);
        query.setParameter("p_last_name", lastName);
        query.setParameter("p_egn", egn);
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

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }

    public List<Client> findByCityId(Long cityId) {
        return clientRepository.findByCity_Id(cityId);
    }

    public List<Client> findByNameContaining(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Client> findByLastNameContaining(String lastName) {
        return clientRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    public Client findByEgn(String egn) {
        return clientRepository.findByEgn(egn)
                .orElseThrow(() ->new RuntimeException("Client with this EGN doesn't exist: " + egn));
    }
}