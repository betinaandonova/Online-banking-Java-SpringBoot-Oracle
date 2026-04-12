package main.dto.employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequest {
    private String name;
    private String lastName;
    private String position;
    private String phoneNumber;
}