package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.OtpSendDTO;
import edu.ijse.gamingLounge.dto.OtpVerifyDTO;
import edu.ijse.gamingLounge.entity.Customer;
import edu.ijse.gamingLounge.entity.Otp;
import edu.ijse.gamingLounge.repository.CustomerRepository;
import edu.ijse.gamingLounge.repository.OtpRepository;
import edu.ijse.gamingLounge.util.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    private static final int EXPIRY_MINUTES = 5;

    @Override
    public void sendOtp(OtpSendDTO dto) {
        try {
            Optional<Customer> customerOptional = customerRepository.findById(dto.getCustomerId());

            if (customerOptional.isEmpty()) {
                log.error("Customer with id {} not found", dto.getCustomerId());
                return;
            }

            Customer customer = customerOptional.get();

            String code = generateSixDigitCode();

            Otp otp = new Otp();

            otp.setCustomer(customer);
            otp.setOtpCode(code);
            otp.setPurpose("REGISTRATION");
            otp.setExpiryTime(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES));
            otp.setVerified(false);

            otpRepository.save(otp);
            log.info("OTP Generated For Customer {} ", customer.getEmail());

            String body = "Hello " + customer.getName() + ",\n\n"
                    +"Your Gaming Lounge Verification Code Is : " + code + "\n"
                    +"This Code Expires In " + EXPIRY_MINUTES + " Minutes.\n\n"
                    +"If You Didn't Request This, You Can Ignore This E-Mail.";
            emailService.sendEmail(customer.getEmail(), "Your Gaming Lounge OTP Code ",body);
        } catch (Exception e) {
            log.error("Error while sending OTP for Customer {} ", dto.getCustomerId(), e);
        }
    }

    @Override
    public boolean verifyOtp(OtpVerifyDTO dto) {
        try {
            Optional<Otp> otpOptional = otpRepository.findLatestByCustomerId(dto.getCustomerId());

            if (otpOptional.isEmpty()) {
                log.error("No OTP Found For Customer {} ", dto.getCustomerId());
                return false;
            }

            Otp otp = otpOptional.get();

            if (otp.getVerified()) {
                log.error("OTP Already Verified For This Customer {} ", dto.getCustomerId());
                return false;
            }

            if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
                log.error("OTP Expired For This Customer {} ", dto.getCustomerId());
                return false;
            }

            if (!otp.getOtpCode().equals(dto.getOtpCode())) {
                log.error("OTP Code Is Incorrect For This Customer {} ", dto.getCustomerId());
                return false;
            }

            otp.setVerified(true);
            otpRepository.save(otp);

            Customer customer = otp.getCustomer();
            customer.setEmailVerified(true);

            customerRepository.save(customer);

            log.info("OTP Verified Successfully For Customer {} ", dto.getCustomerId());
            return true;
        } catch (Exception e) {
            log.error("Error while verifying OTP", e);
            return false;
        }
    }

    private String generateSixDigitCode() {
        SecureRandom secureRandom = new SecureRandom();
        int number = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(number);
    }
}
