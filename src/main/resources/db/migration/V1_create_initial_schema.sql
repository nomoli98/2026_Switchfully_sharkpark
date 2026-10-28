CREATE  TABLE division (
    division_id int PRIMARY KEY,
    name VARCHAR(50) NOT NULL  UNIQUE,
    original_name varchar(50) not null,
    fk_director_id int

)
