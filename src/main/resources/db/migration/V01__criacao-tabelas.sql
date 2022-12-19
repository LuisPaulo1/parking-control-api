create table tb_parking_spot (
   id uuid not null,
	apartment varchar(30) not null,
	block varchar(30) not null,
	brand_car varchar(70) not null,
	color_car varchar(70) not null,
	criation_date timestamp not null,
	license_plate_car varchar(7) not null,
	model_car varchar(70) not null,
	parking_spot_number varchar(10) not null,
	responsible_name varchar(130) not null,
	update_date timestamp not null,
	primary key (id)
);

create table tb_role (
   role_id uuid not null,
	role_name varchar(255) not null,
	primary key (role_id)
);

create table tb_user (
   user_id uuid not null,
	password varchar(255) not null,
	username varchar(255) not null,
	primary key (user_id)
);

create table tb_users_roles (
   user_id uuid not null,
	role_id uuid not null
);

alter table if exists tb_parking_spot
   add constraint UK_license_plate_car unique (license_plate_car);

alter table if exists tb_parking_spot
   add constraint UK_parking_spot_number unique (parking_spot_number);

alter table if exists tb_role
   add constraint UK_role_name unique (role_name);

alter table if exists tb_user
   add constraint UK_username unique (username);

alter table if exists tb_users_roles
   add constraint FK_role_id
   foreign key (role_id)
   references tb_role;

alter table if exists tb_users_roles
   add constraint FK_user_id
   foreign key (user_id)
   references tb_user;