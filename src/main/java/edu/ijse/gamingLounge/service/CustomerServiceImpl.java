package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.CustomerDTO;
import edu.ijse.gamingLounge.entity.Customer;
import edu.ijse.gamingLounge.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Long saveCustomer(CustomerDTO dto) {
        try {
            if (customerRepository.existsByEmail(dto.getEmail())) {
                log.error("A customer with this email already exists: {}", dto.getEmail());
                return null;
            }
            Customer customer = new Customer();
            customer.setName(dto.getName());
            customer.setEmail(dto.getEmail());
            customer.setPhone(dto.getPhone());
            customer.setPassword(dto.getPassword());
            customer.setAddress(dto.getAddress());
            Customer saved = customerRepository.save(customer);
            log.info("Customer saved successfully to the database");
            return saved.getId();
        } catch (Exception e) {
            log.error("Customer saving got an error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void updateCustomer(CustomerDTO dto) {
        try {
            Optional<Customer> optional = customerRepository.findById(dto.getId());
            if (optional.isPresent()) {
                Customer customer = optional.get();
                customer.setName(dto.getName());
                customer.setEmail(dto.getEmail());
                customer.setPhone(dto.getPhone());
                customer.setAddress(dto.getAddress());
                customerRepository.save(customer);
                log.info("Customer updated successfully...");
            }
        } catch (Exception e) {
            log.error("Customer updation failed: {}", e.getMessage());
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        try {
            if (customerRepository.existsById(id)) {
                customerRepository.deleteById(id);
                log.info("Customer deleted successfully from database");
            }
        } catch (Exception e) {
            log.error("Customer deletion failed: {}", e.getMessage());
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> list = new ArrayList<>();
        try {
            for (Customer c : customerRepository.findAll()) {
                list.add(toDTO(c));
            }
            log.info("All customers retrieved successfully from the database");
        } catch (Exception e) {
            log.error("Couldn't retrieve customers from the database", e.getMessage());
        }
        return list;
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        try {
            Optional<Customer> optional = customerRepository.findById(id);
            if (optional.isPresent()) {
                log.info("Customer retrieved successfully from the database");
                return toDTO(optional.get());
            }
        } catch (Exception e) {
            log.error("Couldn't retrieve customer from the database: {}", e.getMessage());
        }
        return null;
    }

    private CustomerDTO toDTO(Customer c) {
        return new CustomerDTO(c.getId(), c.getName(), c.getEmail(), c.getPhone(), null, c.getAddress());
    }
}
