package edu.ijse.gamingLounge.sheduler;

import edu.ijse.gamingLounge.repository.BookingRepository;
import edu.ijse.gamingLounge.service.BookingService;
import edu.ijse.gamingLounge.status.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingAutoCompleteScheduler {
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    @Scheduled(fixedRate = 5 * 60 * 1000) // every 5 minutes
    public void autoCompletePastBookings() {
        var overdue = bookingRepository.findByStatusAndEndTimeBefore(BookingStatus.BOOKED, LocalDateTime.now());
        if (overdue.isEmpty()) {
            return;
        }
        log.info("Auto-completing {} booking(s) whose end time has passed", overdue.size());
        for (var booking : overdue) {
            bookingService.updateBookingStatus(booking.getId(), "COMPLETED");
        }
    }
}

