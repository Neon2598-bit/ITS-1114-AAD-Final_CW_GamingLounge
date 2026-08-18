package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.BookingDTO;

import java.util.List;

public interface BookingService {
    void saveBooking(BookingDTO dto);
    void updateBookingStatus(Long id, String status);
    List<BookingDTO> getAllBookings();
    BookingDTO getBookingById(Long id);
    List<BookingDTO> getBookingsByCustomer(Long customerId);
}
