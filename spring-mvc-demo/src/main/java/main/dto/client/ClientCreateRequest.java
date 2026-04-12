package main.dto.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCreateRequest {
    private String name;
    private String lastName;
    private String egn;
    private String phoneNumber;
    private String address;
    private Long cityId;
}