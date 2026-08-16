package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.FeedbackDTO;
import edu.ijse.gamingLounge.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<CommonResponse> save(@Valid @RequestBody FeedbackDTO dto) {
        feedbackService.saveFeedback(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(ResponseCode.CREATED, ResponseMessage.SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAll() {
        List<FeedbackDTO> list = feedbackService.getAllFeedback();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @GetMapping("/by-booking/{bookingId}")
    public ResponseEntity<CommonResponse> getByBooking(@PathVariable Long bookingId) {
        List<FeedbackDTO> list = feedbackService.getFeedbackByBooking(bookingId);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    // GET /api/v1/feedback/average-rating
    @GetMapping("/average-rating")
    public ResponseEntity<CommonResponse> getAverageRating() {
        Double average = feedbackService.getAverageRating();
        if (average == null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(ResponseCode.SUCCESS, 0.0, "No feedback submitted yet"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, average, ResponseMessage.SUCCESS));
    }
}
