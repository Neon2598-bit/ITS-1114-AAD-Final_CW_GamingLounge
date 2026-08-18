package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.OtpSendDTO;
import edu.ijse.gamingLounge.dto.OtpVerifyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {
    
    @Override
    public void sendOtp(OtpSendDTO dto) {

    }

    @Override
    public boolean verifyOtp(OtpVerifyDTO dto) {
        return false;
    }
}
