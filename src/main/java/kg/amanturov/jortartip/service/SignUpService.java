package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.EmployeeSIgnUpDto;
import kg.amanturov.jortartip.dto.securityDto.LoginResponse;

import java.util.List;

public interface SignUpService {
    void saveEmployee(List<EmployeeSIgnUpDto> dtos);

    LoginResponse findByUserName(String userName);
}
