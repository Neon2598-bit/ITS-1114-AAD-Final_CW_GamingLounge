package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.ChatRequestDTO;
import edu.ijse.gamingLounge.dto.ChatResponseDTO;
import edu.ijse.gamingLounge.service.ChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
@CrossOrigin
public class ChatbotController {
    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<CommonResponse> ask(@Valid @RequestBody ChatRequestDTO dto) {
        String reply = chatbotService.getReply(dto.getMessage());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, new ChatResponseDTO(reply), ResponseMessage.SUCCESS));
    }
}
