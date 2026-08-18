package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.BookingDTO;
import edu.ijse.gamingLounge.entity.Booking;
import edu.ijse.gamingLounge.entity.Customer;
import edu.ijse.gamingLounge.entity.Membership;
import edu.ijse.gamingLounge.entity.Station;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.BookingRepository;
import edu.ijse.gamingLounge.repository.CustomerRepository;
import edu.ijse.gamingLounge.repository.MembershipRepository;
import edu.ijse.gamingLounge.repository.StationRepository;
import edu.ijse.gamingLounge.status.BookingStatus;
import edu.ijse.gamingLounge.status.MembershipStatus;
import edu.ijse.gamingLounge.status.StationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final StationRepository stationRepository;
    private final MembershipRepository membershipRepository;

    @Override
    @Transactional
    public void saveBooking(BookingDTO dto) {
        Optional<Customer> customerOptional = customerRepository.findById(dto.getCustomerId());
        Optional<Station> stationOptional = stationRepository.findById(dto.getStationId());

        if (customerOptional.isEmpty() || stationOptional.isEmpty()) {
            log.error("Customer or Station not found");
            throw new BusinessException("Customer or station not found.");
        }

        if (dto.getStartTime() == null || dto.getEndTime() == null || !dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new BusinessException("End time must be after start time.");
        }
        if (dto.getStartTime().isBefore(java.time.LocalDateTime.now())) {
            throw new BusinessException("Start time cannot be in the past.");
        }

        Station station = stationOptional.get();

        // Business rule: you can't book a station that's already occupied or under maintenance
        if (station.getStatus() != StationStatus.AVAILABLE) {
            log.error("Station {} is not available for booking", station.getStationCode());
            throw new BusinessException(
                    "Station " + station.getStationCode() + " is not available for booking.");
        }

        // Calculate the price: number of hours booked x the station type's hourly rate
        double hours = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes() / 60.0;
        double totalAmount = hours * station.getStationType().getHourlyRate();


        Optional<Membership> activeMembership =
                membershipRepository.findFirstByCustomer_IdAndStatus(
                        dto.getCustomerId(), MembershipStatus.ACTIVE);
        if (activeMembership.isPresent()) {
            double discountPercentage = activeMembership.get().getMembershipPlan().getDiscountPercentage();
            totalAmount = totalAmount * (1 - discountPercentage / 100.0);
            log.info("Applied {}% membership discount for customer {}", discountPercentage, dto.getCustomerId());
        }

        Booking booking = new Booking();
        booking.setCustomer(customerOptional.get());
        booking.setStation(station);
        booking.setStartTime(dto.getStartTime());
        booking.setEndTime(dto.getEndTime());
        booking.setStatus(BookingStatus.BOOKED);
        booking.setTotalAmount(totalAmount);
        bookingRepository.save(booking);
        log.info("Booking saved successfully for station {}", station.getStationCode());

        station.setStatus(StationStatus.OCCUPIED);
        stationRepository.save(station);
        log.info("Station {} status updated to OCCUPIED", station.getStationCode());
    }

    @Override
    @Transactional
    public void updateBookingStatus(Long id, String status) {
        Optional<Booking> optional = bookingRepository.findById(id);
        if (optional.isEmpty()) {
            log.error("Booking not found: {}", id);
            throw new BusinessException("Booking not found.");
        }

        Booking booking = optional.get();
        BookingStatus newStatus;
        try {
            newStatus = BookingStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid booking status: " + status);
        }
        booking.setStatus(newStatus);
        bookingRepository.save(booking);
        log.info("Booking {} status updated to {}", id, newStatus);

        if (newStatus == BookingStatus.COMPLETED || newStatus == BookingStatus.CANCELLED) {
            Station station = booking.getStation();
            station.setStatus(StationStatus.AVAILABLE);
            stationRepository.save(station);
            log.info("Station {} status updated back to AVAILABLE", station.getStationCode());
        }
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        List<BookingDTO> list = new ArrayList<>();
        try {
            for (Booking b : bookingRepository.findAllWithDetails()) {
                list.add(toDTO(b));
            }
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        try {
            Optional<Booking> optional = bookingRepository.findById(id);
            if (optional.isPresent()) {
                return toDTO(optional.get());
            }
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<BookingDTO> getBookingsByCustomer(Long customerId) {
        List<BookingDTO> list = new ArrayList<>();
        try {
            for (Booking b : bookingRepository.findByCustomer_Id(customerId)) {
                list.add(toDTO(b));
            }
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return list;
    }

    private BookingDTO toDTO(Booking b) {
        return new BookingDTO(
                b.getId(),
                b.getStartTime(),
                b.getEndTime(),
                b.getStatus().name(),
                b.getTotalAmount(),
                b.getCustomer().getId(),
                b.getStation().getId(),
                b.getCustomer().getName(),
                b.getStation().getStationCode()
        );
    }
}
