ALTER TABLE users
    ALTER COLUMN registration_date TYPE DATE
        USING registration_date::DATE;