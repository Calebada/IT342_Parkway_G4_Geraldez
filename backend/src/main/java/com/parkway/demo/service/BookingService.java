package com.parkway.demo.service;

import com.parkway.demo.dto.BookingDTO;
import com.parkway.demo.dto.BookingRequest;
import com.parkway.demo.model.Admin;
import com.parkway.demo.model.Booking;
import com.parkway.demo.model.ParkingSlot;
import com.parkway.demo.model.User;
import com.parkway.demo.repository.AdminRepository;
import com.parkway.demo.repository.BookingRepository;
import com.parkway.demo.repository.ParkingSlotRepository;
import com.parkway.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;
    
    @Autowired
    private com.parkway.demo.repository.VehicleRepository vehicleRepository;

    @Autowired
    private NotificationService notificationService;
    
    /**
     * Get all bookings for a specific user
     */
    public List<BookingDTO> getUserBookings(Long userId) {
        try {
            logger.info("Fetching bookings for user ID: {}", userId);
            
            List<Booking> bookings = bookingRepository.findByUserIdWithDetails(userId);
            
            if (bookings.isEmpty()) {
                logger.info("No bookings found for user ID: {}", userId);
            } else {
                logger.info("Found {} booking(s) for user ID: {}", bookings.size(), userId);
            }
            
            return bookings.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error fetching bookings for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error fetching bookings: " + e.getMessage());
        }
    }
    
    /**
     * Create a new booking
     */
    @Transactional
    public Booking createBooking(BookingRequest request) {
        try {
            logger.info("Creating new booking for user ID: {}, parking lot ID: {}", 
                       request.getUserId(), request.getParkingLotId());
            
            // Validate user exists
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
            
            // Validate parking lot (admin) exists
            Admin admin = adminRepository.findById(request.getParkingLotId())
                    .orElseThrow(() -> new RuntimeException("Parking lot not found with id: " + request.getParkingLotId()));
            
            // Check if parking lot has available slots
            long totalSlots = parkingSlotRepository.findByAdmin_AdminIdOrderBySlotNumberAsc(request.getParkingLotId()).size();
            long occupiedSlots = parkingSlotRepository.findByAdmin_AdminIdOrderBySlotNumberAsc(request.getParkingLotId())
                    .stream()
                    .filter(slot -> "occupied".equals(slot.getStatus()))
                    .count();
            
            logger.info("Parking lot {}: {}/{} slots occupied", request.getParkingLotId(), occupiedSlots, totalSlots);
            
            if (totalSlots == 0) {
                throw new RuntimeException("Parking lot has no slots configured");
            }
            
            if (occupiedSlots >= totalSlots) {
                throw new RuntimeException("No available parking slots at this location");
            }
            
            // Create booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setAdmin(admin);
            booking.setStatus("pending");
            booking.setVehicleType(request.getVehicleType());
            
            // Set defaults for optional fields if not provided
            if (request.getDateReserved() != null) {
                booking.setDateReserved(request.getDateReserved());
            } else {
                booking.setDateReserved(java.time.LocalDate.now());
            }
            
            if (request.getTimeIn() != null) {
                booking.setTimeIn(request.getTimeIn());
            } else {
                booking.setTimeIn(java.time.LocalTime.of(8, 0)); // Default 08:00
            }
            
            if (request.getTimeOut() != null) {
                booking.setTimeOut(request.getTimeOut());
            } else {
                booking.setTimeOut(java.time.LocalTime.of(18, 0)); // Default 18:00
            }
            
            if (request.getDuration() != null) {
                booking.setDuration(request.getDuration());
            } else {
                booking.setDuration(10); // Default 10 hours
            }
            
            if (request.getTotalPrice() != null) {
                booking.setTotalPrice(request.getTotalPrice());
            } else {
                booking.setTotalPrice(java.math.BigDecimal.ZERO);
            }
            
            Booking savedBooking = bookingRepository.save(booking);
            logger.info("Booking created successfully with ID: {}", savedBooking.getBookingId());
            
            // Create notification for admin
            try {
                notificationService.createNotification(
                        admin.getAdminId(),
                        "admin",
                        savedBooking.getBookingId(),
                        admin.getAdminId(),
                        "BOOKING_CREATED",
                        "New Booking Request",
                        "New booking request from " + user.getFirstname() + " " + user.getLastname()
                );
            } catch (Exception notificationError) {
                logger.error("Failed to create admin notification for booking {}: {}",
                        savedBooking.getBookingId(), notificationError.getMessage());
            }
            
            return savedBooking;
            
        } catch (Exception e) {
            logger.error("Error creating booking: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Confirm a booking (change status from pending to confirmed)
     * Also assigns a parking slot with full user and vehicle information
     */
    @Transactional
    public void confirmBooking(Long bookingId) {
        try {
            logger.info("Confirming booking ID: {}", bookingId);
            
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
            
            // Check if already confirmed
            if ("confirmed".equals(booking.getStatus())) {
                throw new RuntimeException("Booking is already confirmed");
            }
            
            // Get user information
            User user = booking.getUser();
            if (user == null) {
                throw new RuntimeException("User not found for booking");
            }
            
            // Get vehicle information if available
            // Confirm should still work even if the user has not registered a vehicle record yet
            com.parkway.demo.model.Vehicle vehicle = vehicleRepository.findByUser_UserID(user.getUserID())
                    .orElse(null);
            
            // Find first vacant slot
            Optional<ParkingSlot> vacantSlotOpt = parkingSlotRepository
                    .findFirstByAdmin_AdminIdAndStatusOrderBySlotNumberAsc(booking.getAdmin().getStaffID(), "vacant");
            
            if (!vacantSlotOpt.isPresent()) {
                throw new RuntimeException("No vacant parking slots available");
            }
            
            ParkingSlot slot = vacantSlotOpt.get();
            
            // Update slot with all information
            slot.setStatus("occupied");
            slot.setReserved(true);
            slot.setBookingId(bookingId);
            slot.setUserId(user.getUserID());
            slot.setVehicleId(vehicle != null ? vehicle.getVehicleID() : null);
            slot.setUserFirstname(user.getFirstname());
            slot.setUserLastname(user.getLastname());
            slot.setVehicleType(vehicle != null ? vehicle.getVehicleType() : booking.getVehicleType());
            slot.setVehicleModel(vehicle != null ? vehicle.getModel() : null);
            slot.setPlateNumber(vehicle != null ? vehicle.getPlateNumber() : null);
            
            parkingSlotRepository.save(slot);
            logger.info("Assigned parking slot {} to booking {} with user {} and vehicle {}", 
                       slot.getSlotNumber(), bookingId, user.getFirstname(), vehicle != null ? vehicle.getPlateNumber() : null);
            
            // Update booking with vehicle ID, slot ID and status
            booking.setVehicleId(vehicle != null ? vehicle.getVehicleID() : null);
            booking.setSlotId(slot.getSlotId());
            booking.setStatus("confirmed");
            bookingRepository.save(booking);

            try {
                String parkingLotName = booking.getAdmin().getParkingLotName() != null
                        ? booking.getAdmin().getParkingLotName()
                        : "the selected parking lot";

                notificationService.createNotification(
                        user.getUserID(),
                        "user",
                        booking.getBookingId(),
                        booking.getAdmin().getAdminId(),
                        "BOOKING_APPROVED",
                        "Booking Approved",
                        "Your booking at " + parkingLotName + " has been approved."
                );
            } catch (Exception notificationError) {
                logger.error("Failed to create user notification for booking {}: {}",
                        booking.getBookingId(), notificationError.getMessage());
            }
            
            logger.info("Booking confirmed successfully: {} - vehicle_id={}, slot_id={}", 
                       bookingId, vehicle != null ? vehicle.getVehicleID() : null, slot.getSlotId());
            
        } catch (Exception e) {
            logger.error("Error confirming booking {}: {}", bookingId, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Delete a booking and free up parking slot if confirmed
     */
    @Transactional
    public void deleteBooking(Long bookingId) {
        try {
            logger.info("Deleting booking ID: {}", bookingId);
            
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
            
            // If booking was confirmed, free up the parking slot
            if ("confirmed".equals(booking.getStatus()) && booking.getSlotId() != null) {
                parkingSlotRepository.findById(booking.getSlotId())
                    .ifPresent(slot -> {
                        freeSlot(slot);
                        logger.info("Freed parking slot {} after deleting booking {}", 
                                   slot.getSlotNumber(), bookingId);
                    });
            }
            
            bookingRepository.deleteById(bookingId);
            logger.info("Booking deleted successfully: {}", bookingId);
            
        } catch (Exception e) {
            logger.error("Error deleting booking {}: {}", bookingId, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Get all bookings for a specific admin/parking lot
     */
    public List<com.parkway.demo.dto.AdminBookingDTO> getAdminBookings(Long adminId) {
        try {
            logger.info("Fetching bookings for admin ID: {}", adminId);
            
            List<Booking> bookings = bookingRepository.findByAdminIdWithDetails(adminId);
            
            if (bookings.isEmpty()) {
                logger.info("No bookings found for admin ID: {}", adminId);
            } else {
                logger.info("Found {} booking(s) for admin ID: {}", bookings.size(), adminId);
            }
            
            return bookings.stream()
                    .map(this::convertToAdminDTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error fetching bookings for admin ID {}: {}", adminId, e.getMessage(), e);
            throw new RuntimeException("Error fetching bookings: " + e.getMessage());
        }
    }
    
    /**
     * Convert Booking entity to BookingDTO
     */
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setUserId(booking.getUser() != null ? booking.getUser().getUserID() : null);
        dto.setParkingLotId(booking.getAdmin() != null ? booking.getAdmin().getAdminId() : null);
        dto.setParkingLotName(booking.getAdmin() != null ? booking.getAdmin().getParkingLotName() : null);
        dto.setVehicleType(booking.getVehicleType());
        dto.setStatus(booking.getStatus());
        return dto;
    }
    
    /**
     * Convert Booking entity to AdminBookingDTO with user details
     */
    private com.parkway.demo.dto.AdminBookingDTO convertToAdminDTO(Booking booking) {
        com.parkway.demo.dto.AdminBookingDTO dto = new com.parkway.demo.dto.AdminBookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setUserId(booking.getUser() != null ? booking.getUser().getUserID() : null);
        dto.setParkingLotId(booking.getAdmin() != null ? booking.getAdmin().getAdminId() : null);
        dto.setParkingLotName(booking.getAdmin() != null ? booking.getAdmin().getParkingLotName() : null);
        dto.setUserFirstname(booking.getUser() != null ? booking.getUser().getFirstname() : null);
        dto.setUserLastname(booking.getUser() != null ? booking.getUser().getLastname() : null);
        dto.setVehicleType(booking.getVehicleType());
        dto.setStatus(booking.getStatus());
        return dto;
    }
    
    /**
     * Free a parking slot
     */
    private void freeSlot(ParkingSlot slot) {
        slot.setStatus("vacant");
        slot.setReserved(false);
        slot.setBookingId(null);
        slot.setUserId(null);
        slot.setVehicleId(null);
        slot.setUserFirstname(null);
        slot.setUserLastname(null);
        slot.setVehicleType(null);
        slot.setVehicleModel(null);
        slot.setPlateNumber(null);
        parkingSlotRepository.save(slot);
    }
}
