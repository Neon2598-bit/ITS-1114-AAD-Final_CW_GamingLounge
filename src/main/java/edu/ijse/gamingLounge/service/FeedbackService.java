package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.FeedbackDTO;

import java.util.List;

public interface FeedbackService {
    void saveFeedback(FeedbackDTO dto);
    List<FeedbackDTO> getAllFeedback();
    List<FeedbackDTO> getFeedbackByBooking(Long bookingId);
    Double getAverageRating();
}
