package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.dto.OtpSendDTO;
import edu.ijse.gamingLounge.dto.OtpVerifyDTO;
import edu.ijse.gamingLounge.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
@CrossOrigin
public class OtpController {
    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<CommonResponse> send(@Valid @RequestBody OtpSendDTO dto) {
        otpService.sendOtp(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, "OTP sent to your email"));
    }

    @PostMapping("/verify")
    public ResponseEntity<CommonResponse> verify(@Valid @RequestBody OtpVerifyDTO dto) {
        boolean isValid = otpService.verifyOtp(dto);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(ResponseCode.BAD_REQUEST, "Invalid or expired OTP"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, "OTP verified successfully"));
    }
}
