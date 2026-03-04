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
    
    /**
     * Get all bookings for a specific user
     * Returns empty list if no bookings found (NOT an error)
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
            logger.info("Creating booking for user ID: {} at parking lot ID: {}", 
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
                throw new RuntimeException("Parking lot is already full. No available slots at this moment.");
            }
            
            // Create booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setAdmin(admin);
            booking.setDateReserved(request.getDateReserved());
            booking.setTimeIn(request.getTimeIn());
            booking.setTimeOut(request.getTimeOut());
            booking.setVehicleType(request.getVehicleType());
            booking.setDuration(request.getDuration());
            booking.setTotalPrice(request.getTotalPrice());
            booking.setStatus("pending");
            
            Booking savedBooking = bookingRepository.save(booking);
            logger.info("Booking created successfully with ID: {}", savedBooking.getBookingId());
            
            return savedBooking;
            
        } catch (Exception e) {
            logger.error("Error creating booking: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Get all bookings for admin's parking lot
     * Returns empty list if no bookings found
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
            
            // Get vehicle information
            com.parkway.demo.model.Vehicle vehicle = vehicleRepository.findByUser_UserID(user.getUserID())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found for user ID: " + user.getUserID()));
            
            // Find first vacant slot
            Optional<ParkingSlot> vacantSlotOpt = parkingSlotRepository
                    .findFirstVacantSlot(booking.getAdmin().getStaffID());
            
            if (!vacantSlotOpt.isPresent()) {
                throw new RuntimeException("No vacant parking slots available");
            }
            
            ParkingSlot slot = vacantSlotOpt.get();
            
            // Update slot with all information
            slot.setStatus("occupied");
            slot.setReserved(true);
            slot.setBookingId(bookingId);
            slot.setUserId(user.getUserID());
            slot.setVehicleId(vehicle.getVehicleID());
            slot.setUserFirstname(user.getFirstname());
            slot.setUserLastname(user.getLastname());
            slot.setVehicleType(vehicle.getVehicleType());
            slot.setVehicleModel(vehicle.getModel());
            slot.setPlateNumber(vehicle.getPlateNumber());
            
            parkingSlotRepository.save(slot);
            logger.info("Assigned parking slot {} to booking {} with user {} and vehicle {}", 
                       slot.getSlotNumber(), bookingId, user.getFirstname(), vehicle.getPlateNumber());
            
            // Update booking with vehicle ID, slot ID and status
            booking.setVehicleId(vehicle.getVehicleID());
            booking.setSlotId(slot.getSlotId());
            booking.setStatus("confirmed");
            bookingRepository.save(booking);
            
            logger.info("Booking confirmed successfully: {} - vehicle_id={}, slot_id={}", 
                       bookingId, vehicle.getVehicleID(), slot.getSlotId());
            
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
            
            // Delete the booking
            bookingRepository.deleteById(bookingId);
            logger.info("Booking deleted successfully: {}", bookingId);
            
        } catch (Exception e) {
            logger.error("Error deleting booking {}: {}", bookingId, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Free a parking slot and clear all information
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
    
    /**
     * Convert Booking entity to BookingDTO with parking lot name
     */
    private BookingDTO convertToDTO(Booking booking) {
        return new BookingDTO(
                booking.getBookingId(),
                booking.getUser().getUserID(),
                booking.getAdmin().getStaffID(),
                booking.getAdmin().getParkingLotName(),
                booking.getDateReserved(),
                booking.getTimeIn(),
                booking.getTimeOut(),
                booking.getVehicleType(),
                booking.getDuration(),
                booking.getTotalPrice(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }
    
    /**
     * Convert Booking entity to AdminBookingDTO with user details
     */
    private com.parkway.demo.dto.AdminBookingDTO convertToAdminDTO(Booking booking) {
        return new com.parkway.demo.dto.AdminBookingDTO(
                booking.getBookingId(),
                booking.getUser().getUserID(),
                booking.getAdmin().getStaffID(),
                booking.getAdmin().getParkingLotName(),
                booking.getUser().getFirstname(),
                booking.getUser().getLastname(),
                booking.getDateReserved(),
                booking.getTimeIn(),
                booking.getTimeOut(),
                booking.getVehicleType(),
                booking.getDuration(),
                booking.getTotalPrice(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }
}
