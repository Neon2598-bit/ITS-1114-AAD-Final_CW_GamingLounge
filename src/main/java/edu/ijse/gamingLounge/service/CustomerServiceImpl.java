package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.CustomerDTO;
import edu.ijse.gamingLounge.entity.Customer;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long saveCustomer(CustomerDTO dto) {
        try {
            if (customerRepository.existsByEmail(dto.getEmail())) {
                log.error("A customer with this email already exists: {}", dto.getEmail());
                throw new BusinessException("Customer with this email already exists", HttpStatus.CONFLICT);
            }
            Customer customer = new Customer();
            customer.setName(dto.getName());
            customer.setEmail(dto.getEmail());
            if (customerRepository.existsByPhone(dto.getPhone())) {
                throw new BusinessException("This phone number already exists", HttpStatus.CONFLICT);
            }
            customer.setPhone(dto.getPhone());
            // Hash the password BEFORE saving
            customer.setPassword(passwordEncoder.encode(dto.getPassword()));

            customer.setAddress(dto.getAddress());
            Customer saved = customerRepository.save(customer);
            log.info("Customer saved successfully to the database");
            return saved.getId();
        } catch (Exception e) {
            log.error("Customer saving got an error: {}", e.getMessage());
            throw new  BusinessException("Customer saving got an error", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public void updateCustomer(CustomerDTO dto) {
        Optional<Customer> customerOptional = customerRepository.findById(dto.getId());
        if (customerOptional.isEmpty()) {
            throw new BusinessException("Customer not found", HttpStatus.NOT_FOUND);
        }

        if (!customerOptional.get().isActive()) {
            throw new BusinessException("Customer not active", HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerOptional.get();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        if (customerRepository.existsByPhone(dto.getPhone())) {
            throw new BusinessException("This phone number already exists", HttpStatus.CONFLICT);
        }
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customerRepository.save(customer);
        log.info("Customer updated successfully...");
    }

    @Override
    public void deleteCustomer(Long id) {
        try {
            Optional<Customer> optional = customerRepository.findById(id);
            if (optional.isEmpty()) {
                throw new BusinessException("Customer not found", HttpStatus.NOT_FOUND);
            }
            Customer customer = optional.get();
            customer.setActive(false);
            customerRepository.save(customer);
            log.info("Customer marked as inactive.");
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> list = new ArrayList<>();
        try {
            for (Customer c : customerRepository.findByActiveTrue()) {
                list.add(toDTO(c));
            }
            log.info("Successfully retrieve all customers from database");
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        try {
            Optional<Customer> optional = customerRepository.findById(id);
            if (optional.isPresent()) {
                return toDTO(optional.get());
            }
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public void restoreCustomer(Long id) {
        Optional<Customer> optional = customerRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BusinessException("Customer not found", HttpStatus.NOT_FOUND);
        }
        if (optional.get().isActive()) {
            throw new  BusinessException("Customer is already active", HttpStatus.BAD_REQUEST);
        }
        Customer customer = optional.get();
        customer.setActive(true);
        customerRepository.save(customer);
        log.info("Customer restored");
    }

    @Override
    public List<CustomerDTO> getInactiveCustomers() {
        List<CustomerDTO> list = new ArrayList<>();
        try {
            for (Customer c : customerRepository.findByActiveFalse()) {
                list.add(toDTO(c));
            }
            log.info("Successfully retrieve inactive customers from database");
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return list;
    }

    private CustomerDTO toDTO(Customer c) {
        return new CustomerDTO(c.getId(), c.getName(), c.getEmail(), c.getPhone(), null, c.getAddress());
    }
}
