package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.OtpSendDTO;
import edu.ijse.gamingLounge.dto.OtpVerifyDTO;

public interface OtpService {
    void sendOtp(OtpSendDTO dto);
    boolean verifyOtp(OtpVerifyDTO dto);
}
