package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.EmployeeSIgnUpDto;

import java.util.List;

public interface SignUpService {
    void saveEmployee(List<EmployeeSIgnUpDto> dtos);
}
