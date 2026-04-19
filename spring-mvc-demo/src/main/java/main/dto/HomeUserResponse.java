package main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeUserResponse {

    private String username;
    private String fullName;
    private String egn;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private List<AccountResponse> accounts;
}