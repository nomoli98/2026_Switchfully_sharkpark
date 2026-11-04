CREATE TABLE invoices
(
    invoice_id SERIAL PRIMARY KEY,
    created_at DATE    NOT NULL,
    expired_at DATE,
    status     VARCHAR NOT NULL,
    payment_at DATE    NOT NULL,
    fk_user_id INT,
    FOREIGN KEY (fk_user_id) REFERENCES users (user_id)
);

CREATE TABLE invoice_items
(
    invoice_item_id          SERIAL PRIMARY KEY,
    fk_parking_allocation_id INT,
    item_price               DECIMAL,
    fk_invoice_id            INT,
    FOREIGN KEY (fk_parking_allocation_id) REFERENCES parking_allocations (parking_allocation_id),
    FOREIGN KEY (fk_invoice_id) REFERENCES invoices (invoice_id)

)
