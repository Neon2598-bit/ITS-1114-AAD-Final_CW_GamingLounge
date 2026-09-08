package edu.ijse.gamingLounge.config;

import edu.ijse.gamingLounge.entity.Employee;
import edu.ijse.gamingLounge.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_EMAIL = "erandamadushan.1998@gmail.com";
    private static final String ADMIN_PASSWORD = "Admin@123";

    @Override
    public void run(String... args) {
        try {
            if (!employeeRepository.existsByEmail(ADMIN_EMAIL)) {
                Employee admin = new Employee();
                admin.setName("System Admin");
                admin.setEmail(ADMIN_EMAIL);
                admin.setPhone("0704048645");
                admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));

                employeeRepository.save(admin);
                log.info("Default admin created -> email: {}, password: {}", ADMIN_EMAIL, ADMIN_PASSWORD);
            } else {
                log.info("Admin already exists, skipping seeding.");
            }
        } catch (Exception e) {
            log.error("Admin seeding failed: {}", e.getMessage());
        }
    }
}
