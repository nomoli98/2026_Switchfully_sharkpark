ALTER TABLE divisions ADD COLUMN fk_subdivision_id INT;
ALTER TABLE divisions ADD CONSTRAINT fk_divisions_subdivisions FOREIGN KEY (fk_subdivision_id) REFERENCES divisions(division_id);