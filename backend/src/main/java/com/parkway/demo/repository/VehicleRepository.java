package com.parkway.demo.repository;

import com.parkway.demo.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    // Find vehicle by user ID
    Optional<Vehicle> findByUser_UserID(Long userId);
    
    // Check if plate number exists
    boolean existsByPlateNumber(String plateNumber);
    
    // Find vehicle by plate number
    Optional<Vehicle> findByPlateNumber(String plateNumber);
    
    // Check if plate number exists excluding specific vehicle ID (for updates)
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Vehicle v WHERE v.plateNumber = :plateNumber AND v.vehicleID != :vehicleId")
    boolean existsByPlateNumberAndNotVehicleId(@Param("plateNumber") String plateNumber, @Param("vehicleId") Long vehicleId);
}
