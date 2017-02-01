create table User (id int primary key);
create table Account (id int identity,
						user_id int unique not null,
						username varchar unique,
						password varchar not null,
						firstName varchar not null, 
						lastName varchar not null,
						primary key (id));						
ALTER TABLE Account ADD FOREIGN KEY(user_id) REFERENCES User(id);					
