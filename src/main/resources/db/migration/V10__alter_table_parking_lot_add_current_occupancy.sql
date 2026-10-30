ALTER TABLE parking_lots ADD COLUMN current_occupancy INT DEFAULT 0;
ALTER TABLE parking_lots ADD CONSTRAINT chk_occupancy CHECK (current_occupancy >= 0 AND current_occupancy <= max_capacity);