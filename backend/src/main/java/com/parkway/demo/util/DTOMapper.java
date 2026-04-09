package com.parkway.demo.util;

import com.parkway.demo.dto.BookingDTO;
import com.parkway.demo.dto.VehicleDTO;
import com.parkway.demo.dto.UserDTO;
import com.parkway.demo.dto.ParkingSlotDTO;
import com.parkway.demo.model.Booking;
import com.parkway.demo.model.Vehicle;
import com.parkway.demo.model.User;
import com.parkway.demo.model.ParkingSlot;
import org.springframework.stereotype.Component;

/**
 * DTO Mapper Factory
 * 
 * PATTERN: Factory
 * 
 * PROBLEM SOLVED:
 * - 5 different classes had duplicate convertToDTO() implementations
 * - BookingService, VehicleService, UserController, ParkingSlotService, AdminService
 *   all manually mapped entities to DTOs
 * - Changes to DTOs required updates in 5+ locations
 * - ~50 lines of duplicate code
 * 
 * BENEFITS:
 * - Single source of truth for all DTO conversions
 * - Easy to add new mappers (just add method)
 * - Can easily integrate ModelMapper or MapStruct later
 * - Reduces code duplication
 * - Testable mapping logic
 */
@Component
public class DTOMapper {
    
    /**
     * Convert Booking entity to BookingDTO
     */
    public BookingDTO toBookingDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        
        String parkingLotName = booking.getAdmin() != null ? 
            booking.getAdmin().getParkingLotName() : null;
        
        return new BookingDTO(
            booking.getBookingId(),
            booking.getUser() != null ? booking.getUser().getUserID() : null,
            booking.getAdmin() != null ? booking.getAdmin().getAdminId() : null,
            parkingLotName,
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
     * Convert Vehicle entity to VehicleDTO
     */
    public VehicleDTO toVehicleDTO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        
        return new VehicleDTO(
            vehicle.getVehicleID(),
            vehicle.getUser() != null ? vehicle.getUser().getUserID() : null,
            vehicle.getPlateNumber(),
            vehicle.getModel(),
            vehicle.getVehicleType(),
            vehicle.getCreatedAt()
        );
    }
    
    /**
     * Convert User entity to UserDTO (exclude sensitive data)
     */
    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserDTO(
            user.getUserID(),
            user.getFirstname(),
            user.getLastname(),
            user.getEmail()
        );
    }
    
    /**
     * Convert ParkingSlot entity to ParkingSlotDTO
     */
    public ParkingSlotDTO toParkingSlotDTO(ParkingSlot slot) {
        if (slot == null) {
            return null;
        }
        
        return new ParkingSlotDTO(
            slot.getSlotId(),
            slot.getAdmin() != null ? slot.getAdmin().getAdminId() : null,
            slot.getSlotNumber(),
            slot.getStatus()
        );
    }
}
