-- =========================================
-- FIX VEHICLE TABLE SCHEMA
-- =========================================
-- This script drops and recreates the vehicles table
-- to match the new JPA entity structure
-- =========================================

USE parkway;

-- Drop existing vehicles table if it exists
DROP TABLE IF EXISTS vehicles;

-- Verify users table exists and check structure
SELECT 'Checking users table structure...' as Status;
DESCRIBE users;

-- Create vehicles table with correct schema
CREATE TABLE vehicles (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    plate_number VARCHAR(50) NOT NULL UNIQUE,
    model VARCHAR(100) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vehicle_user FOREIGN KEY (user_id) 
        REFERENCES users(user_id) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add indexes for better performance
CREATE INDEX idx_user_id ON vehicles(user_id);
CREATE INDEX idx_plate_number ON vehicles(plate_number);

-- Verify table creation
SELECT 'Vehicles table created successfully!' as Status;
DESCRIBE vehicles;
SHOW CREATE TABLE vehicles;
