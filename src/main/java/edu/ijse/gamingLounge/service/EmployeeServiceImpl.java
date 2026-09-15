package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.EmployeeDTO;
import edu.ijse.gamingLounge.entity.Employee;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveEmployee(EmployeeDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            log.error("An employee with this email already exists: {}", dto.getEmail());
            throw new BusinessException(
                    "An employee with this email already exists.");
        }
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        // Hash the password BEFORE saving
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));

        employeeRepository.save(employee);
        log.info("Employee saved successfully to database");
    }

    @Override
    public void updateEmployee(EmployeeDTO dto) {
        Optional<Employee> optional = employeeRepository.findById(dto.getId());
        if (optional.isEmpty()) {
            throw new BusinessException("Employee not found.");
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
        Optional<Employee> optional = employeeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BusinessException("Employee not found.");
        }
        Employee employee = optional.get();
        employee.setActive(false);
        employeeRepository.save(employee);
        log.info("Employee marked as inactive.");
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> list = new ArrayList<>();
        try {
            for (Employee e : employeeRepository.findByActiveTrue()) {
                list.add(toDTO(e));
            }
            log.info("All employees found successfully from database");
        } catch (Exception e) {
            log.error("Couldn't fetch employees from the database", e.getMessage());
        }
        return list;
    }

    @Override
    public List<EmployeeDTO> getInactiveEmployees() {
        List<EmployeeDTO> list = new ArrayList<>();
        try {
            for (Employee e : employeeRepository.findByActiveFalse()) {
                list.add(toDTO(e));
            }
            log.info("Successfully retrieved inactive employees from database");
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public void restoreEmployee(Long id) {
        Optional<Employee> optional = employeeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BusinessException("Employee not found.");
        }
        if (optional.get().isActive()) {
            throw new BusinessException("Employee is already active.");
        }
        Employee employee = optional.get();
        employee.setActive(true);
        employeeRepository.save(employee);
        log.info("Employee restored.");
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
