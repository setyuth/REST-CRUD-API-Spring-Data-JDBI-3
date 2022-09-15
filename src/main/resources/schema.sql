create table if not exists jdbi3_crud.product (
	
	id bigserial primary key,
	name varchar(100) NOT NULL,
	description varchar(255),
	price numeric(10,2),
	qty integer

);