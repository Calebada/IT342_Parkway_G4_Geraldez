-- =========================================
-- FIX BOOKINGS TABLE SCHEMA
-- Add missing columns to match Java model
-- =========================================

USE parkway;

-- Add missing columns to bookings table
ALTER TABLE bookings ADD COLUMN vehicle_id INT NULL AFTER parking_lot_id;
ALTER TABLE bookings ADD COLUMN slot_id INT NULL AFTER vehicle_id;

-- Add foreign key constraints
ALTER TABLE bookings
    ADD CONSTRAINT fk_booking_vehicle 
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE SET NULL;

ALTER TABLE bookings
    ADD CONSTRAINT fk_booking_slot 
    FOREIGN KEY (slot_id) REFERENCES parking_slots(slot_id) ON DELETE SET NULL;

-- Add indexes for better performance
CREATE INDEX idx_bookings_vehicle_id ON bookings(vehicle_id);
CREATE INDEX idx_bookings_slot_id ON bookings(slot_id);

-- Verify the updated structure
DESCRIBE bookings;

-- Show sample data
SELECT * FROM bookings LIMIT 5;
