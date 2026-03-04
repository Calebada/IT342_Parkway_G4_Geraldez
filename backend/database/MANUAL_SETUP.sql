-- =========================================
-- MANUAL DATABASE SETUP FOR VEHICLES TABLE
-- =========================================
-- Execute these commands in MySQL Workbench or phpMyAdmin
-- =========================================

USE parkway;

-- Step 1: Remove old vehicles table completely
DROP TABLE IF EXISTS vehicles;

-- Step 2: Create new vehicles table
CREATE TABLE vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    plate_number VARCHAR(50) NOT NULL,
    model VARCHAR(100) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_plate_number (plate_number),
    KEY idx_user_id (user_id),
    CONSTRAINT fk_vehicles_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Step 3: Verify the table was created
DESCRIBE vehicles;

-- Step 4: Test by inserting sample data (optional)
-- Make sure user with user_id=1 exists first!
-- INSERT INTO vehicles (user_id, plate_number, model, vehicle_type) 
-- VALUES (1, 'ABC 1234', 'Toyota Vios', 'Car');

-- Step 5: Verify the insert worked
-- SELECT * FROM vehicles;

SELECT 'Table created successfully!' AS Status;
