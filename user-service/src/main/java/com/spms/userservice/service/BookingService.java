package com.spms.userservice.service;

import com.spms.userservice.dto.BookingRequest;
import com.spms.userservice.dto.BookingResponse;
import com.spms.userservice.entity.Booking;
import com.spms.userservice.entity.User;
import com.spms.userservice.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;

    public BookingService(BookingRepository bookingRepository, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
    }

    public BookingResponse addBooking(Long userId, BookingRequest request) {
        User user = userService.findUserOrThrow(userId);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVehicleId(request.getVehicleId());
        booking.setParkingSpaceId(request.getParkingSpaceId());
        booking.setLocation(request.getLocation());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setAmount(request.getAmount());
        if (request.getStatus() != null) {
            booking.setStatus(request.getStatus());
        }

        Booking saved = bookingRepository.save(booking);
        return new BookingResponse(saved);
    }

    public List<BookingResponse> getBookingHistory(Long userId) {
        // Ensures a clean 404 if the user doesn't exist, rather than
        // silently returning an empty list.
        userService.findUserOrThrow(userId);

        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(BookingResponse::new)
                .toList();
    }
}
