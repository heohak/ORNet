ALTER TABLE client_worker
ADD CONSTRAINT fk_client
FOREIGN KEY (client_id)
REFERENCES client (id);
