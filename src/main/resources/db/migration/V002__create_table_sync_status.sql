create table sync_status (
	table_name varchar(100) not null,
	last_sync_time timestamp not null
)