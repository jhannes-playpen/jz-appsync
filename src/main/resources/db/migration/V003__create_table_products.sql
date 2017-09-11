create table products (
	id uuid primary key,
	product_name varchar(100) not null,
	product_category varchar(30),
	price_in_cents number,
	updated_at timestamp not null
);
