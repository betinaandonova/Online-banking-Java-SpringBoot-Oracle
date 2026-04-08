package main.procedure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientProcedureService {

    @PersistenceContext
    private EntityManager entityManager;

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
        query.registerStoredProcedureParameter("p_phone_number", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_address", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_city_id", Long.class, ParameterMode.IN);

        query.setParameter("p_name", name);
        query.setParameter("p_last_name", lastName);
        query.setParameter("p_egn", egn);
        query.setParameter("p_phone_number", phoneNumber);
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
        query.registerStoredProcedureParameter("p_phone_number", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_address", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_city_id", Long.class, ParameterMode.IN);

        query.setParameter("p_client_id", clientId);
        query.setParameter("p_name", name);
        query.setParameter("p_last_name", lastName);
        query.setParameter("p_egn", egn);
        query.setParameter("p_phone_number", phoneNumber);
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
}