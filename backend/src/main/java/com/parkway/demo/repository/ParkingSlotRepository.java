package com.parkway.demo.repository;

import com.parkway.demo.model.Admin;
import com.parkway.demo.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    
    /**
     * Find all parking slots for a specific parking lot
     */
    List<ParkingSlot> findByAdmin_AdminIdOrderBySlotNumberAsc(Long parkingLotId);
    
    /**
     * Find first vacant slot for a parking lot
     */
    Optional<ParkingSlot> findFirstByAdmin_AdminIdAndStatusOrderBySlotNumberAsc(Long parkingLotId, String status);
    
    /**
     * Find first slot by admin ID and status
     */
    Optional<ParkingSlot> findFirstByAdmin_AdminIdAndStatus(Long adminId, String status);
    
    /**
     * Find all slots for an admin
     */
    List<ParkingSlot> findByAdmin(Admin admin);
    
    /**
     * Find slot by booking ID
     */
    Optional<ParkingSlot> findByBookingId(Long bookingId);
}
