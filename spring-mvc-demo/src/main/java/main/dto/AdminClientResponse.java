package main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminClientResponse {

    private Long id;
    private String name;
    private String lastName;
    private String egn;
    private String phone;
    private String address;
    private String city;
    private String country;
}