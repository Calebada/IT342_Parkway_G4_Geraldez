-- =========================================
-- FIX PARKING_SLOTS TABLE SCHEMA
-- Add missing columns to match Java model
-- =========================================

USE parkway;

-- Add missing columns to parking_slots table (one at a time)
ALTER TABLE parking_slots ADD COLUMN reserved BOOLEAN DEFAULT FALSE AFTER status;
ALTER TABLE parking_slots ADD COLUMN booking_id INT NULL AFTER reserved;
ALTER TABLE parking_slots ADD COLUMN user_id INT NULL AFTER booking_id;
ALTER TABLE parking_slots ADD COLUMN vehicle_id INT NULL AFTER user_id;
ALTER TABLE parking_slots ADD COLUMN user_firstname VARCHAR(100) NULL AFTER vehicle_id;
ALTER TABLE parking_slots ADD COLUMN user_lastname VARCHAR(100) NULL AFTER user_firstname;
ALTER TABLE parking_slots ADD COLUMN vehicle_type VARCHAR(50) NULL AFTER user_lastname;
ALTER TABLE parking_slots ADD COLUMN vehicle_model VARCHAR(100) NULL AFTER vehicle_type;
ALTER TABLE parking_slots ADD COLUMN plate_number VARCHAR(50) NULL AFTER vehicle_model;

-- Add foreign key constraints for the new columns
ALTER TABLE parking_slots
    ADD CONSTRAINT fk_parking_slot_booking 
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE SET NULL;

ALTER TABLE parking_slots
    ADD CONSTRAINT fk_parking_slot_user 
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL;

ALTER TABLE parking_slots
    ADD CONSTRAINT fk_parking_slot_vehicle 
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE SET NULL;

-- Add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_parking_slots_booking_id ON parking_slots(booking_id);
CREATE INDEX IF NOT EXISTS idx_parking_slots_user_id ON parking_slots(user_id);
CREATE INDEX IF NOT EXISTS idx_parking_slots_vehicle_id ON parking_slots(vehicle_id);

-- Verify the updated structure
DESCRIBE parking_slots;

-- Show sample data
SELECT * FROM parking_slots LIMIT 5;
