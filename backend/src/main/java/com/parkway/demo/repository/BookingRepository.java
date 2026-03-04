package com.parkway.demo.repository;

import com.parkway.demo.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Find all bookings for a specific user, ordered by date and time descending
     */
    @Query("SELECT b FROM Booking b " +
           "LEFT JOIN FETCH b.user u " +
           "LEFT JOIN FETCH b.admin a " +
           "WHERE b.user.userID = :userId " +
           "ORDER BY b.dateReserved DESC, b.timeIn DESC")
    List<Booking> findByUserIdWithDetails(@Param("userId") Long userId);
    
    /**
     * Find all bookings for admin's parking lot by admin ID
     */
    @Query("SELECT b FROM Booking b " +
           "LEFT JOIN FETCH b.user u " +
           "LEFT JOIN FETCH b.admin a " +
           "WHERE a.adminId = :adminId " +
           "ORDER BY b.dateReserved DESC, b.timeIn DESC")
    List<Booking> findByAdminIdWithDetails(@Param("adminId") Long adminId);
}
