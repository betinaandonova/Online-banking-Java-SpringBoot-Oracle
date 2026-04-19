package main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private String userType; // EMPLOYEE / CLIENT

    private String name;
    private String lastName;
    private String phoneNumber;

    // client only
    private String address;
    private String city;
    private String country;

    // employee only
    private String position;
}