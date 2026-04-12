package main.dto.onlinebankinguser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnlineBankingUserUpdateRequest {
    private Long clientId;
    private Long employeeId;
    private String username;
    private String passwordHash;
}