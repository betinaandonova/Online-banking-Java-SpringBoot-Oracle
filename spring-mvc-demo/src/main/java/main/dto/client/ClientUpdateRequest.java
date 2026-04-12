package main.dto.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientUpdateRequest {
    private String name;
    private String lastName;
    private String phoneNumber;
    private String address;
    private Long cityId;
}