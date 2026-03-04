-- =========================================
-- VEHICLE MANAGEMENT SYSTEM - DATABASE SCHEMA
-- =========================================
-- This script creates the vehicles table with proper relationships
-- Execute this in your MySQL database: parkway
-- =========================================

-- Create vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    plate_number VARCHAR(50) NOT NULL UNIQUE,
    model VARCHAR(100) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_plate_number (plate_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- SAMPLE DATA FOR TESTING
-- =========================================
-- Uncomment the lines below if you want to insert test data
-- Make sure the user_id values exist in your users table

-- INSERT INTO vehicles (user_id, plate_number, model, vehicle_type) VALUES
-- (1, 'ABC 1234', 'Toyota Vios', 'Car'),
-- (2, 'XYZ 5678', 'Honda Civic', 'Car'),
-- (3, 'DEF 9012', 'Yamaha Mio', 'Motorcycle');

-- =========================================
-- VERIFICATION QUERIES
-- =========================================
-- Check if table was created successfully
-- SHOW CREATE TABLE vehicles;

-- View all vehicles
-- SELECT * FROM vehicles;

-- View vehicles with user information
-- SELECT 
--     v.vehicle_id,
--     v.user_id,
--     v.plate_number,
--     v.model,
--     v.vehicle_type,
--     v.created_at,
--     u.firstname,
--     u.lastname,
--     u.email
-- FROM vehicles v
-- JOIN users u ON v.user_id = u.user_id;
