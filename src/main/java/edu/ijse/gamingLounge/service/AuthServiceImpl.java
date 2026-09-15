package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.ForgotPasswordDTO;
import edu.ijse.gamingLounge.dto.LoginDTO;
import edu.ijse.gamingLounge.dto.LoginResponseDTO;
import edu.ijse.gamingLounge.dto.ResetPasswordDTO;
import edu.ijse.gamingLounge.entity.Customer;
import edu.ijse.gamingLounge.entity.Employee;
import edu.ijse.gamingLounge.entity.Otp;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.CustomerRepository;
import edu.ijse.gamingLounge.repository.EmployeeRepository;
import edu.ijse.gamingLounge.repository.OtpRepository;
import edu.ijse.gamingLounge.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;
    private final OtpRepository otpRepository;
    @Override
    public LoginResponseDTO login(LoginDTO dto) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findByEmail(dto.getEmail());
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                if (passwordEncoder.matches(dto.getPassword(), employee.getPassword())) {
                    String token = jwtUtil.generateToken(employee.getEmail(), "ADMIN");
                    log.info("Employee logged in: {}", employee.getEmail());
                    return new LoginResponseDTO(employee.getId(), token, employee.getEmail(), "ADMIN", employee.getName());
                }
                log.error("Incorrect password for employee: {}", dto.getEmail());
                return null;
            }

            Optional<Customer> customerOptional = customerRepository.findByEmail(dto.getEmail());
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                if (passwordEncoder.matches(dto.getPassword(), customer.getPassword())) {
                    if (!customer.isActive()) {
                        log.error("Login blocked - account deactivated: {}", dto.getEmail());
                        throw new BusinessException(
                                "This account has been deactivated. Please contact support.",
                                HttpStatus.FORBIDDEN);
                    }
                    if (!customer.isEmailVerified()) {
                        log.error("Login blocked - email not verified: {}", dto.getEmail());
                        throw new BusinessException(
                                "Please verify your email with the OTP sent to you before logging in.",
                                HttpStatus.FORBIDDEN);
                    }
                    String token = jwtUtil.generateToken(customer.getEmail(), "USER");
                    log.info("Customer logged in: {}", customer.getEmail());
                    return new LoginResponseDTO(customer.getId(), token, customer.getEmail(), "USER", customer.getName());
                }
                log.error("Incorrect password for customer: {}", dto.getEmail());
                return null;
            }

            log.error("No account found with email: {}", dto.getEmail());
            return null;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void forgotPassword(ForgotPasswordDTO dto) {
        otpService.sendPasswordResetOtp(dto.getEmail());
    }

    @Override
    public void resetPassword(ResetPasswordDTO dto) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(dto.getEmail());
        if (customerOptional.isEmpty()) {
            throw new BusinessException("No account found with this email.", HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerOptional.get();

        Optional<Otp> otpOptional = otpRepository.findLatestByCustomerIdAndPurpose(customer.getId(), "PASSWORD_RESET");
        if (otpOptional.isEmpty()) {
            throw new BusinessException("No password reset request found. Please request a new code.", HttpStatus.BAD_REQUEST);
        }
        Otp otp = otpOptional.get();

        if (otp.getVerified()) {
            throw new BusinessException("This code has already been used. Please request a new one.", HttpStatus.BAD_REQUEST);
        }
        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("This code has expired. Please request a new one.", HttpStatus.BAD_REQUEST);
        }
        if (!otp.getOtpCode().equals(dto.getOtpCode())) {
            throw new BusinessException("Incorrect code.", HttpStatus.BAD_REQUEST);
        }

        otp.setVerified(true);
        otpRepository.save(otp);

        customer.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        customerRepository.save(customer);

        log.info("Password reset successfully for {}", customer.getEmail());
    }

    @Override
    public String guestLogin() {
        log.info("Guest token issued");
        return jwtUtil.generateToken("guest", "GUEST");
    }
}