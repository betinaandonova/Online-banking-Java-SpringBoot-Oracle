package main.repository;

import main.model.OnlineBankingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OnlineBankingUserRepository extends JpaRepository<OnlineBankingUser, Long> {

    Optional<OnlineBankingUser> findByUsername(String username);



}