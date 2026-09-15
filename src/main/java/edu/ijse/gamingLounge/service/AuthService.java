package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.ForgotPasswordDTO;
import edu.ijse.gamingLounge.dto.LoginDTO;
import edu.ijse.gamingLounge.dto.LoginResponseDTO;
import edu.ijse.gamingLounge.dto.ResetPasswordDTO;

public interface AuthService {
    LoginResponseDTO login(LoginDTO dto);

    String guestLogin();

    void forgotPassword(ForgotPasswordDTO dto);
    void resetPassword(ResetPasswordDTO dto);
}
