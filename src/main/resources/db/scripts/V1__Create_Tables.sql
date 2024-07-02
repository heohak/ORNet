CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    short_name VARCHAR(255),
    third_party VARCHAR(255)
);

CREATE TABLE client_worker (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(255),
    title VARCHAR(255),
    client_id INTEGER
);
