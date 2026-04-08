package main.repository;

import main.model.OnlineBankingUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnlineBankingUserRepository extends JpaRepository<OnlineBankingUser, Long> {
}