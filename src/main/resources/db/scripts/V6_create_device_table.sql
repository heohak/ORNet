CREATE TABLE public."client" (
    id INTEGER PRIMARY KEY,
    full_name VARCHAR(255),
    short_name VARCHAR(255),
    third_partyit VARCHAR(255)
);

CREATE TABLE public."client_worker" (
    id INTEGER PRIMARY KEY,
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(255),
    title VARCHAR(255),
    client_id INTEGER,
    CONSTRAINT client_worker_client_id_fk FOREIGN KEY (client_id) REFERENCES public."client" (id)
);

CREATE TABLE public."ticket" (
    id INTEGER PRIMARY KEY,
    client_id INTEGER,
    description VARCHAR(255),
    CONSTRAINT client_ticket_client_id_fk FOREIGN KEY (client_id) REFERENCES public."client" (id)
);

CREATE TABLE public."device" (
    id INTEGER PRIMARY KEY,
    client_id INTEGER,
    device_name VARCHAR(255),
    serial_number INTEGER,
    CONSTRAINT client_device_client_id_fk FOREIGN KEY (client_id) REFERENCES public."client" (id)
);