CREATE TABLE parking_allocations(
    parking_allocation_id SERIAL PRIMARY KEY,
    license_plate VARCHAR(12) NOT NULL,
    start_time TIMESTAMP,
    fk_user_id INT,
    fk_parking_lot_id INT,
    FOREIGN KEY (fk_user_id) REFERENCES users(user_id),
    FOREIGN KEY (fk_parking_lot_id) REFERENCES parking_lots(parking_lot_id)
);