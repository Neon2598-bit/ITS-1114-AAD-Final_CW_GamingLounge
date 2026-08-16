package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.FeedbackDTO;
import edu.ijse.gamingLounge.entity.Booking;
import edu.ijse.gamingLounge.entity.Customer;
import edu.ijse.gamingLounge.entity.Feedback;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.BookingRepository;
import edu.ijse.gamingLounge.repository.CustomerRepository;
import edu.ijse.gamingLounge.repository.FeedbackRepository;
import edu.ijse.gamingLounge.status.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;

    @Override
    public void saveFeedback(FeedbackDTO dto) {
        Optional<Customer> customerOptional = customerRepository.findById(dto.getCustomerId());
        Optional<Booking> bookingOptional = bookingRepository.findById(dto.getBookingId());

        if (customerOptional.isEmpty() || bookingOptional.isEmpty()) {
            log.error("Customer or Booking not found");
            throw new BusinessException("Customer or Booking not found");
        }

        Booking booking = bookingOptional.get();

        if (!booking.getCustomer().getId().equals(dto.getCustomerId())) {
            throw new BusinessException("This booking does not belong to this customer");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            log.error("Can not leave feedback - booking {} is not completed yet", booking.getId());
            throw new BusinessException("You can only leave feedback for a completed booking.");
        }

        if (!feedbackRepository.findByBooking_Id(booking.getId()).isEmpty()) {
            throw new BusinessException("You've already left feedback for this booking.");
        }

        Feedback feedback = new Feedback();

        feedback.setCustomer(customerOptional.get());
        feedback.setBooking(booking);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        feedback.setFeedbackDate(LocalDateTime.now());

        feedbackRepository.save(feedback);
        log.info("Feedback saved successfully");
    }

    @Override
    public List<FeedbackDTO> getAllFeedback() {
        List<FeedbackDTO> feedbackDTOList = new ArrayList<>();

        try {
            for (Feedback feedback : feedbackRepository.findAllWithDetails()) {
                feedbackDTOList.add(toDTO(feedback));
            }
            log.info("All feedbacks retrieved successfully");
        } catch (Exception e) {
            log.error("Error while fetching feedback");
        }
        return feedbackDTOList;
    }

    @Override
    public List<FeedbackDTO> getFeedbackByBooking(Long bookingId) {
        return List.of();
    }

    @Override
    public Double getAverageRating() {
        return 0.0;
    }

    private FeedbackDTO toDTO(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getRating(),
                feedback.getComment(),
                feedback.getCustomer().getId(),
                feedback.getBooking().getId(),
                feedback.getFeedbackDate(),
                feedback.getCustomer().getName()
        )
    }
}
