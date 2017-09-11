create table persons (
	id uuid primary key,
	first_name varchar(50) not null,
	middle_name varchar(50) null,
	last_name varchar(50) not null,
	date_of_birth date null,
	updated_at timestamp not null
)