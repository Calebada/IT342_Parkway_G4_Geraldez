-- =========================================
-- COMPLETE DATABASE MIGRATION FIX
-- Handles circular foreign key dependencies
-- =========================================

USE parkway;

-- Step 1: Check and add columns to bookings
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'bookings' AND COLUMN_NAME = 'vehicle_id');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE bookings ADD COLUMN vehicle_id INT NULL AFTER parking_lot_id', 
              'SELECT "vehicle_id already exists in bookings"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'bookings' AND COLUMN_NAME = 'slot_id');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE bookings ADD COLUMN slot_id INT NULL AFTER vehicle_id', 
              'SELECT "slot_id already exists in bookings"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Step 2: Check and add columns to parking_slots
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'parking_slots' AND COLUMN_NAME = 'reserved');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE parking_slots ADD COLUMN reserved BOOLEAN DEFAULT FALSE AFTER status', 
              'SELECT "reserved already exists in parking_slots"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'parking_slots' AND COLUMN_NAME = 'booking_id');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE parking_slots ADD COLUMN booking_id INT NULL AFTER reserved', 
              'SELECT "booking_id already exists in parking_slots"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'parking_slots' AND COLUMN_NAME = 'user_id');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE parking_slots ADD COLUMN user_id INT NULL AFTER booking_id', 
              'SELECT "user_id already exists in parking_slots"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'parking_slots' AND COLUMN_NAME = 'vehicle_id');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE parking_slots ADD COLUMN vehicle_id INT NULL AFTER user_id', 
              'SELECT "vehicle_id already exists in parking_slots"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'parking_slots' AND COLUMN_NAME = 'user_firstname');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE parking_slots ADD COLUMN user_firstname VARCHAR(100) NULL AFTER vehicle_id', 
              'SELECT "user_firstname already exists in parking_slots"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'parking_slots' AND COLUMN_NAME = 'user_lastname');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE parking_slots ADD COLUMN user_lastname VARCHAR(100) NULL AFTER user_firstname', 
              'SELECT "user_lastname already exists in parking_slots"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'parking_slots' AND COLUMN_NAME = 'vehicle_type');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE parking_slots ADD COLUMN vehicle_type VARCHAR(50) NULL AFTER user_lastname', 
              'SELECT "vehicle_type already exists in parking_slots"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'parking_slots' AND COLUMN_NAME = 'vehicle_model');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE parking_slots ADD COLUMN vehicle_model VARCHAR(100) NULL AFTER vehicle_type', 
              'SELECT "vehicle_model already exists in parking_slots"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = 'parkway' AND TABLE_NAME = 'parking_slots' AND COLUMN_NAME = 'plate_number');
SET @sql = IF(@col_exists = 0, 
              'ALTER TABLE parking_slots ADD COLUMN plate_number VARCHAR(50) NULL AFTER vehicle_model', 
              'SELECT "plate_number already exists in parking_slots"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Step 3: Add indexes
CREATE INDEX IF NOT EXISTS idx_bookings_vehicle_id ON bookings(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_bookings_slot_id ON bookings(slot_id);
CREATE INDEX IF NOT EXISTS idx_parking_slots_booking_id ON parking_slots(booking_id);
CREATE INDEX IF NOT EXISTS idx_parking_slots_user_id ON parking_slots(user_id);
CREATE INDEX IF NOT EXISTS idx_parking_slots_vehicle_id ON parking_slots(vehicle_id);

-- Step 4: Add foreign keys (skip if they already exist)
SET @fk_exists = (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS 
                  WHERE CONSTRAINT_SCHEMA = 'parkway' 
                  AND TABLE_NAME = 'bookings' 
                  AND CONSTRAINT_NAME = 'fk_booking_vehicle');

SET @sql = IF(@fk_exists = 0,
    'ALTER TABLE bookings ADD CONSTRAINT fk_booking_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE SET NULL',
    'SELECT "fk_booking_vehicle already exists" AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_exists = (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS 
                  WHERE CONSTRAINT_SCHEMA = 'parkway' 
                  AND TABLE_NAME = 'parking_slots' 
                  AND CONSTRAINT_NAME = 'fk_parking_slot_user');

SET @sql = IF(@fk_exists = 0,
    'ALTER TABLE parking_slots ADD CONSTRAINT fk_parking_slot_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL',
    'SELECT "fk_parking_slot_user already exists" AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_exists = (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS 
                  WHERE CONSTRAINT_SCHEMA = 'parkway' 
                  AND TABLE_NAME = 'parking_slots' 
                  AND CONSTRAINT_NAME = 'fk_parking_slot_vehicle');

SET @sql = IF(@fk_exists = 0,
    'ALTER TABLE parking_slots ADD CONSTRAINT fk_parking_slot_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE SET NULL',
    'SELECT "fk_parking_slot_vehicle already exists" AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verify
DESCRIBE bookings;
DESCRIBE parking_slots;

SELECT 'Migration completed successfully!' AS Status;
