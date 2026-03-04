-- =========================================
-- COMPLETE PARKING SYSTEM DATABASE SCHEMA
-- =========================================
-- ⚠️ USES CREATE TABLE IF NOT EXISTS TO PRESERVE EXISTING DATA
-- This will only create tables if they don't exist
-- NO DATA WILL BE DELETED
-- =========================================

USE parkway;

-- =========================================
-- BOOKINGS TABLE
-- =========================================
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

-- =========================================
-- PARKING_SLOTS TABLE
-- =========================================
CREATE TABLE IF NOT EXISTS parking_slots (
    slot_id INT PRIMARY KEY AUTO_INCREMENT,
    parking_lot_id INT NOT NULL,
    slot_number INT NOT NULL,
    status VARCHAR(20) DEFAULT 'vacant',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parking_lot_id) REFERENCES admins(staff_id),
    UNIQUE KEY unique_slot (parking_lot_id, slot_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- NOTIFICATIONS TABLE
-- =========================================
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    booking_id INT,
    parking_lot_id INT,
    title VARCHAR(255),
    message TEXT,
    type VARCHAR(50),
    `read` BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id),
    FOREIGN KEY (parking_lot_id) REFERENCES admins(staff_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- ADD INDEXES FOR BETTER PERFORMANCE
-- =========================================
CREATE INDEX IF NOT EXISTS idx_bookings_user_id ON bookings(user_id);
CREATE INDEX IF NOT EXISTS idx_bookings_parking_lot_id ON bookings(parking_lot_id);
CREATE INDEX IF NOT EXISTS idx_bookings_date_reserved ON bookings(date_reserved);
CREATE INDEX IF NOT EXISTS idx_bookings_status ON bookings(status);

CREATE INDEX IF NOT EXISTS idx_parking_slots_parking_lot_id ON parking_slots(parking_lot_id);
CREATE INDEX IF NOT EXISTS idx_parking_slots_status ON parking_slots(status);

CREATE INDEX IF NOT EXISTS idx_notifications_user_id ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_parking_lot_id ON notifications(parking_lot_id);
CREATE INDEX IF NOT EXISTS idx_notifications_read ON notifications(`read`);

-- =========================================
-- VERIFY TABLES
-- =========================================
SHOW TABLES;

SELECT 'Bookings table structure:' as Info;
DESCRIBE bookings;

SELECT 'Parking slots table structure:' as Info;
DESCRIBE parking_slots;

SELECT 'Notifications table structure:' as Info;
DESCRIBE notifications;

-- =========================================
-- CHECK EXISTING DATA (NO DATA DELETED)
-- =========================================
SELECT COUNT(*) as total_users FROM users;
SELECT COUNT(*) as total_admins FROM admins;
SELECT COUNT(*) as total_vehicles FROM vehicles;
SELECT COUNT(*) as total_bookings FROM bookings;
SELECT COUNT(*) as total_parking_slots FROM parking_slots;
SELECT COUNT(*) as total_notifications FROM notifications;
