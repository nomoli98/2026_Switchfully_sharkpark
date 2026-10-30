alter table parking_lots
    ADD COLUMN name varchar(50),
    add column price_per_hour decimal;


ALTER TABLE parking_lots
    ALTER COLUMN address_street SET NOT NULL,
    ALTER COLUMN address_number SET NOT NULL;