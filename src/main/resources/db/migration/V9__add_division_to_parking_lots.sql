ALTER TABLE parking_lots
ADD COLUMN fk_division_id INT;

ALTER TABLE parking_lots
ADD CONSTRAINT fk_parking_lots_division FOREIGN KEY (fk_division_id) REFERENCES divisions(division_id);

BEGIN;
UPDATE parking_lots
    SET fk_division_id = 1 WHERE fk_division_id IS NULL ;

ALTER TABLE parking_lots
    ALTER COLUMN fk_division_id SET NOT NULL;

COMMIT ;


