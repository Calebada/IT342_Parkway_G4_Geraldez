-- Verify database schema
USE parkway;

-- Check if columns exist in bookings table
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'parkway' 
  AND TABLE_NAME = 'bookings'
ORDER BY ORDINAL_POSITION;

-- Check if columns exist in parking_slots table
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'parkway' 
  AND TABLE_NAME = 'parking_slots'
ORDER BY ORDINAL_POSITION;
