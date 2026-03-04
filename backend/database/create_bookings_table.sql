-- =========================================
-- BOOKINGS TABLE SCHEMA
-- =========================================
-- ⚠️ USES CREATE TABLE IF NOT EXISTS TO PRESERVE EXISTING DATA
-- This will only create the table if it doesn't exist
-- =========================================

USE parkway;

-- Create bookings table (only if it doesn't exist)
CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    parking_lot_id INT NOT NULL,
    date_reserved DATE NOT NULL,
    time_in TIME NOT NULL,
    time_out TIME NOT NULL,
    vehicle_type VARCHAR(50) NOT NULL,
    duration INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (parking_lot_id) REFERENCES admins(staff_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add indexes for better performance (only if they don't exist)
CREATE INDEX IF NOT EXISTS idx_user_id ON bookings(user_id);
CREATE INDEX IF NOT EXISTS idx_parking_lot_id ON bookings(parking_lot_id);
CREATE INDEX IF NOT EXISTS idx_date_reserved ON bookings(date_reserved);
CREATE INDEX IF NOT EXISTS idx_status ON bookings(status);

-- Verify table structure
DESCRIBE bookings;

-- Check existing bookings (will show all data if any exists)
SELECT COUNT(*) as total_bookings FROM bookings;
SELECT * FROM bookings ORDER BY created_at DESC LIMIT 10;

-- =========================================
-- SAMPLE TEST DATA (COMMENTED OUT)
-- Uncomment only if you want to add test data
-- =========================================
/*
-- Insert sample booking (make sure user_id and parking_lot_id exist)
INSERT INTO bookings (user_id, parking_lot_id, date_reserved, time_in, time_out, vehicle_type, duration, total_price, status)
VALUES (1, 1, '2025-12-05', '08:00:00', '17:00:00', 'Car', 9, 225.00, 'pending');
*/
