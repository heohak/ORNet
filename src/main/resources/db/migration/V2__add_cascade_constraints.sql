ALTER TABLE client_location_aud
DROP CONSTRAINT fkov6g0u1xu35yrxpx1vhd1il4m;
ALTER TABLE client_location_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE client_maintenance_aud
DROP CONSTRAINT fkpfv6sgsixdajta8u0084kuyjo;
ALTER TABLE client_maintenance_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE client_third_party_it_aud
DROP CONSTRAINT fktaevq37rj74ujauu8araclp61;
ALTER TABLE client_third_party_it_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE client_worker_role_aud
DROP CONSTRAINT fkqg68ook4rln4evm0btv3wt0w4;
ALTER TABLE client_worker_role_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE device_classificator_aud
DROP CONSTRAINT fksumx6v3g49ubch818rjaqhdsg;
ALTER TABLE device_classificator_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE device_comment_aud
DROP CONSTRAINT fkfi4yaavqwnv1wbrig82iig6n0;
ALTER TABLE device_comment_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE device_file_upload_aud
DROP CONSTRAINT fkp13v3opvjx0b3ktws0km92cek;
ALTER TABLE device_file_upload_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE device_maintenance_aud
DROP CONSTRAINT fknrww1a9sc3mseflkifrv2y3r0;
ALTER TABLE device_maintenance_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE linked_device_comment_aud
DROP CONSTRAINT fk9rkc97462dct0ad85viun1y5h;
ALTER TABLE linked_device_comment_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE maintenance_file_upload_aud
DROP CONSTRAINT fkhirm2teqn4nugay20s00dtgjk;
ALTER TABLE maintenance_file_upload_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE ticket_client_worker_aud
DROP CONSTRAINT fko1dllmavwbdjvfixbv0e3smu5;
ALTER TABLE ticket_client_worker_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE ticket_comment_aud
DROP CONSTRAINT fkjhc9bfpiens9gcsqyw77mf6g1;
ALTER TABLE ticket_comment_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE ticket_file_upload_aud
DROP CONSTRAINT fk2al0lvbcv4x89pt9ard7dgrhy;
ALTER TABLE ticket_file_upload_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE ticket_maintenance_aud
DROP CONSTRAINT fkpnfe0b0wroh2qdep06tdgbr45;
ALTER TABLE ticket_maintenance_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;

ALTER TABLE ticket_work_type_classificator_aud
DROP CONSTRAINT fkbo9k2yh0sqv322org51kqk70o;
ALTER TABLE ticket_work_type_classificator_aud
ADD CONSTRAINT fk_revinfo
FOREIGN KEY (rev) REFERENCES revinfo(rev) ON DELETE CASCADE;
