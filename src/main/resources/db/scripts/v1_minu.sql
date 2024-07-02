-- Create client-test table
CREATE TABLE public."client-test" (
                                      id INTEGER PRIMARY KEY,
                                      full_name VARCHAR(255),
                                      short_name VARCHAR(255),
                                      third_party_it VARCHAR(255)
);

-- Create client_worker-test table
CREATE TABLE public."client_worker-test" (
                                             id INTEGER PRIMARY KEY,
                                             email VARCHAR(255),
                                             first_name VARCHAR(255),
                                             last_name VARCHAR(255),
                                             phone_number VARCHAR(255),
                                             title VARCHAR(255),
                                             client_id INTEGER,
                                             CONSTRAINT client_worker_client_id_fk FOREIGN KEY (client_id) REFERENCES public."client-test" (id)
);
