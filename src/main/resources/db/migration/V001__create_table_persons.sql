create table persons (
	id number primary key auto_increment,
	first_name varchar(50) not null,
	middle_name varchar(50) not null,
	last_name varchar(50) not null,
	date_of_birth date not null
)