package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    void saveEmployee(EmployeeDTO dto);
    void updateEmployee(EmployeeDTO dto);
    void deleteEmployee(Long id);
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(Long id);
}
