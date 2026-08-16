package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.FeedbackDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
    @Override
    public void saveFeedback(FeedbackDTO dto) {

    }

    @Override
    public List<FeedbackDTO> getAllFeedback() {
        return List.of();
    }

    @Override
    public List<FeedbackDTO> getFeedbackByBooking(Long bookingId) {
        return List.of();
    }

    @Override
    public Double getAverageRating() {
        return 0.0;
    }
}
