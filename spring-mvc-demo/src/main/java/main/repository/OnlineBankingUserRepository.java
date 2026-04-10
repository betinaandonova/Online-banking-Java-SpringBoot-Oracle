package main.repository;

import main.model.OnlineBankingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface OnlineBankingUserRepository extends JpaRepository<OnlineBankingUser, Long> {

    Optional<OnlineBankingUser> findByUsername(String username);

    Optional<OnlineBankingUser> findByClient_Id(Long clientId);

    Optional<OnlineBankingUser> findByEmployee_Id(Long employeeId);

    List<OnlineBankingUser> findAllByOrderByUsernameAsc();
}