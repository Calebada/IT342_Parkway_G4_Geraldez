package com.parkway.demo.repository;

import com.parkway.demo.model.Admin;
import com.parkway.demo.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT ps FROM ParkingSlot ps WHERE ps.admin.adminId = :parkingLotId AND ps.status = 'vacant' ORDER BY ps.slotNumber ASC")
    Optional<ParkingSlot> findFirstVacantSlot(@Param("parkingLotId") Long parkingLotId);
    
    /**
     * Find first slot by admin ID and status
     */
    @Query("SELECT ps FROM ParkingSlot ps WHERE ps.admin.adminId = :adminId AND ps.status = :status ORDER BY ps.slotNumber ASC")
    Optional<ParkingSlot> findFirstByAdminIdAndStatus(@Param("adminId") Long adminId, @Param("status") String status);
    
    /**
     * Find all slots for an admin
     */
    List<ParkingSlot> findByAdmin(Admin admin);
    
    /**
     * Find slot by booking ID
     */
    Optional<ParkingSlot> findByBookingId(Long bookingId);
}
