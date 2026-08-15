package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.EmployeeDTO;
import edu.ijse.gamingLounge.entity.Employee;
import edu.ijse.gamingLounge.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public void saveEmployee(EmployeeDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            log.error("An employee with this email already exists: {}", dto.getEmail());
            throw new edu.ijse.gamingLounge.exception.BusinessException(
                    "An employee with this email already exists.");
        }
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setPassword(dto.getPassword());
        employeeRepository.save(employee);
        log.info("Employee saved successfully to database");
    }

    @Override
    public void updateEmployee(EmployeeDTO dto) {
        Optional<Employee> optional = employeeRepository.findById(dto.getId());
        if (optional.isEmpty()) {
            throw new edu.ijse.gamingLounge.exception.BusinessException("Employee not found.");
        }
        Employee employee = optional.get();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employeeRepository.save(employee);
        log.info("Employee updated successfully to database");
    }

    @Override
    public void deleteEmployee(Long id) {
        try {
            if (employeeRepository.existsById(id)) {
                employeeRepository.deleteById(id);
                log.info("Employee deleted successfully from database");
            }
        } catch (Exception e) {
            log.error("Deletion failed", e.getMessage());
        }
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> list = new ArrayList<>();
        try {
            for (Employee e : employeeRepository.findAll()) {
                list.add(toDTO(e));
            }
            log.info("All employees found successfully from database");
        } catch (Exception e) {
            log.error("Couldn't fetch employees from the database", e.getMessage());
        }
        return list;
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        try {
            Optional<Employee> optional = employeeRepository.findById(id);
            if (optional.isPresent()) {
                log.info("Employee found successfully from database");
                return toDTO(optional.get());
            }
        } catch (Exception e) {
            log.error("Couldn't retrieve the employee details", e.getMessage());
        }
        return null;
    }

    private EmployeeDTO toDTO(Employee e) {
        return new EmployeeDTO(e.getId(), e.getName(), e.getEmail(), e.getPhone(), null);
    }
}
