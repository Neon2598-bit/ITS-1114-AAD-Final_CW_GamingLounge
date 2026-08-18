package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.LoginDTO;
import edu.ijse.gamingLounge.dto.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginDTO dto);

    // Issues a GUEST-role JWT with no credentials required
    String guestLogin();
}
