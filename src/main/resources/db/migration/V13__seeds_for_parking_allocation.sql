-- Seeds the data added since V5. IDs assume V5 ran on empty tables:
-- users 1-12, divisions 1-6, parking lots 1-13.

-- Memberships: users 6, 7 and 11 stay on the default BRONZE
UPDATE users SET membership = 'SILVER' WHERE user_id IN (8, 9);
UPDATE users SET membership = 'GOLD' WHERE user_id IN (10, 12);

-- V9 put every parking lot in division 1: move them to their real division
UPDATE parking_lots SET fk_division_id = 2 WHERE parking_lot_id IN (3, 4, 12);
UPDATE parking_lots SET fk_division_id = 3 WHERE parking_lot_id IN (5, 6);
UPDATE parking_lots SET fk_division_id = 4 WHERE parking_lot_id IN (7, 8, 9);
UPDATE parking_lots SET fk_division_id = 5 WHERE parking_lot_id IN (10, 11);
UPDATE parking_lots SET fk_division_id = 6 WHERE parking_lot_id = 13;

INSERT INTO parking_allocations (license_plate, start_time, stop_time, status, fk_user_id, fk_parking_lot_id)
VALUES
    -- 1-6: stopped and invoiced (BRONZE members)
    ('2-TCL-614', '2026-08-03 08:30:00', '2026-08-03 11:30:00', 'STOPPED', 6, 12),
    ('2-TCL-614', '2026-08-17 09:00:00', '2026-08-17 13:00:00', 'STOPPED', 6, 12),
    ('2-TCL-614', '2026-09-05 14:00:00', '2026-09-05 16:00:00', 'STOPPED', 6, 3),
    ('2-CLA-723', '2026-08-11 10:00:00', '2026-08-11 12:00:00', 'STOPPED', 7, 8),
    ('2-CLA-723', '2026-09-22 13:30:00', '2026-09-22 16:30:00', 'STOPPED', 7, 8),
    ('2-HJA-167', '2026-08-25 09:15:00', '2026-08-25 12:15:00', 'STOPPED', 11, 3),

    -- 7-12: stopped, not invoiced yet
    ('2-HJA-167', '2026-10-02 17:00:00', '2026-10-02 18:00:00', 'STOPPED', 11, 4),
    ('2-HJA-167', '2026-10-14 08:00:00', '2026-10-14 12:00:00', 'STOPPED', 11, 3),
    ('2-AWI-834', '2026-10-06 09:00:00', '2026-10-06 14:30:00', 'STOPPED', 8, 13),
    ('2-LME-945', '2026-10-13 10:00:00', '2026-10-13 15:00:00', 'STOPPED', 9, 11),
    ('2-MLE-056', '2026-10-20 07:30:00', '2026-10-20 19:30:00', 'STOPPED', 10, 9),
    ('2-VGO-278', '2026-10-27 08:45:00', '2026-10-27 17:15:00', 'STOPPED', 12, 2),

    -- 13-17: still active
    ('2-TCL-614', '2026-11-05 07:45:00', NULL, 'ACTIVE', 6, 12),
    ('2-LME-945', '2026-11-05 08:30:00', NULL, 'ACTIVE', 9, 11),
    ('2-MLE-056', '2026-11-04 18:00:00', NULL, 'ACTIVE', 10, 9),
    ('2-VGO-278', '2026-11-05 09:00:00', NULL, 'ACTIVE', 12, 1),
    ('2-HJA-167', '2026-11-05 08:15:00', NULL, 'ACTIVE', 11, 3);

-- Keep occupancy in line with the active allocations above
UPDATE parking_lots p
SET current_occupancy = (SELECT COUNT(*)
                         FROM parking_allocations a
                         WHERE a.fk_parking_lot_id = p.parking_lot_id
                           AND a.status = 'ACTIVE');

INSERT INTO invoices (created_at, expired_at, status, payment_at, fk_user_id)
VALUES ('2026-09-01', '2026-10-01', 'PAID', '2026-09-12', 6),
       ('2026-09-01', '2026-10-01', 'PAID', '2026-09-20', 7),
       ('2026-09-01', '2026-10-01', 'PAID', '2026-09-08', 11),
       ('2026-10-01', '2026-11-01', 'PAID', '2026-10-09', 6),
       ('2026-10-01', '2026-11-01', 'PAID', '2026-10-15', 7);

-- item_price = hours parked * parking lot price_per_hour
INSERT INTO invoice_items (fk_parking_allocation_id, item_price, fk_invoice_id)
VALUES (1, 4.80, 1),
       (2, 6.40, 1),
       (4, 4.00, 2),
       (6, 10.50, 3),
       (3, 7.00, 4),
       (5, 6.00, 5);