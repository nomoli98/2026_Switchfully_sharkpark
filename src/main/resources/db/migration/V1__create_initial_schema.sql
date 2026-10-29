CREATE TABLE cities
(
    city_id     SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    postal_code INT         NOT NULL UNIQUE
);

CREATE TABLE users
(
    user_id           SERIAL PRIMARY KEY,
    firstname         VARCHAR(100) NOT NULL,
    lastname          VARCHAR(100) NOT NULL,
    password          VARCHAR(100) NOT NULL,
    mobile_number     VARCHAR(12),
    phone_number      VARCHAR(12),
    email             VARCHAR(100) NOT NULL UNIQUE,
    address_street    VARCHAR(150),
    address_number    INT,
    fk_city_id        INT,
    license_plate     VARCHAR(12)  NOT NULL UNIQUE,
    registration_date TIMESTAMP    NOT NULL,
    role              VARCHAR(10)  NOT NULL,

    FOREIGN KEY (fk_city_id) REFERENCES cities (city_id)
);

CREATE TABLE divisions
(
    division_id    SERIAL PRIMARY KEY,
    name           VARCHAR(50) NOT NULL UNIQUE,
    original_name  VARCHAR(50) NOT NULL,
    fk_director_id INT,
    FOREIGN KEY (fk_director_id) REFERENCES users (user_id)
);


CREATE TABLE parking_lots
(
    parking_lot_id       SERIAL PRIMARY KEY,
    category             VARCHAR(25) NOT NULL,
    max_capacity         INT         NOT NULL,
    fk_contact_person_id INT         NOT NULL,
    address_street       VARCHAR(150),
    address_number       INT,
    fk_city_id           INT,
    FOREIGN KEY (fk_contact_person_id) REFERENCES users (user_id),
    FOREIGN KEY (fk_city_id) REFERENCES cities (city_id)
);

