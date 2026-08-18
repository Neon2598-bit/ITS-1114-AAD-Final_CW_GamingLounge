package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.OtpSendDTO;
import edu.ijse.gamingLounge.dto.OtpVerifyDTO;
import edu.ijse.gamingLounge.repository.CustomerRepository;
import edu.ijse.gamingLounge.repository.OtpRepository;
import edu.ijse.gamingLounge.util.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;
    @Override
    public void sendOtp(OtpSendDTO dto) {

    }

    @Override
    public boolean verifyOtp(OtpVerifyDTO dto) {
        return false;
    }
}
