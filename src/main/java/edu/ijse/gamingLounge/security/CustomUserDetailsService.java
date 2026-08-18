package edu.ijse.gamingLounge.security;

import edu.ijse.gamingLounge.entity.Customer;
import edu.ijse.gamingLounge.entity.Employee;
import edu.ijse.gamingLounge.repository.CustomerRepository;
import edu.ijse.gamingLounge.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if ("guest".equals(email)) {
            return User.withUsername("guest")
                    .password("")
                    .authorities("ROLE_GUEST")
                    .build();
        }

        Optional<Employee> employeeOptional = employeeRepository.findByEmail(email);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            return User.withUsername(employee.getEmail())
                    .password(employee.getPassword())
                    .authorities("ROLE_ADMIN")
                    .build();
        }

        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            return User.withUsername(customer.getEmail())
                    .password(customer.getPassword())
                    .authorities("ROLE_USER")
                    .build();
        }

        throw new UsernameNotFoundException("No account found with email: " + email);
    }
}
