delete from tb_users_roles;

delete from tb_user;

delete from tb_role;

delete from tb_parking_spot;

INSERT INTO tb_user (user_id, password, username)
VALUES ('06d2197f-675c-44b8-b8b5-80edce382c82', '$2a$12$z/g2astt0/FGP4PGvLEU/.lnlx/fujP8kNJq9qhFuUbIst1aZ7uwW', 'maria');

INSERT INTO tb_user (user_id, password, username)
VALUES ('46d9819a-597f-45a3-9756-641e4069e7da', '$2a$12$z/g2astt0/FGP4PGvLEU/.lnlx/fujP8kNJq9qhFuUbIst1aZ7uwW', 'joao');

INSERT INTO tb_role (role_id, role_name) VALUES ('b1ca4fe1-2c16-4b34-9522-8cf1e4a2b601', 'ROLE_ADMIN');

INSERT INTO tb_role (role_id, role_name) VALUES ('5bfe80ee-14aa-4a1d-8b72-38ac235e88f3', 'ROLE_USER');

INSERT INTO tb_users_roles (user_id, role_id) values('06d2197f-675c-44b8-b8b5-80edce382c82', 'b1ca4fe1-2c16-4b34-9522-8cf1e4a2b601');

INSERT INTO tb_users_roles (user_id, role_id) values('46d9819a-597f-45a3-9756-641e4069e7da', '5bfe80ee-14aa-4a1d-8b72-38ac235e88f3');

INSERT INTO tb_parking_spot (
    id, apartment, block, brand_car, color_car, criation_date, license_plate_car, model_car, parking_spot_number, responsible_name, update_date)
VALUES ('932eda96-638b-4cad-af31-3a4f91fc3a5e', '202', 'D', 'audi', 'red', current_timestamp, 'RRS8562', 'q5', '205B', 'Bruna', current_timestamp);