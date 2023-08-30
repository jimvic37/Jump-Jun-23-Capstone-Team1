# Creates and uses database
drop database if exists pokemon_db;
create database pokemon_db;
use pokemon_db;
    
# Dummy Data for Trainers
insert into trainer(email, password, role, username)
	values('sample@email.com', 'pw123', 'ROLE_ADMIN', 'ash');
insert into trainer(email, password, role, username)
	values('sample@email.com', 'pw123', 'ROLE_USER', 'brock');
insert into trainer(email, password, role, username)
	values('sample@email.com', 'pw123', 'ROLE_USER', 'misty');
    
# Dummy Data for Pokemon
insert into pokemon(name, number, type_main)
	values('Pikachu', 25, 'ELECTRIC');
insert into pokemon(name, number, type_main)
	values('Squirtle', 7, 'WATER');
insert into pokemon(name, number, type_main, type_secondary)
	values('Bulbasaur', 1, 'GRASS', 'POISON');
insert into pokemon(name, number, type_main)
	values('Charmander', 4, 'FIRE');
    
insert into pokemon(name, number, type_main, type_secondary)
	values('Geodude', 74, 'ROCK', 'GROUND');
insert into pokemon(name, number, type_main, type_secondary)
	values('Onix', 95, 'ROCK', 'GROUND');
insert into pokemon(name, number, type_main)
	values('Diglett', 50, 'ROCK');

insert into pokemon(name, number, type_main)
	values('Staryu', 120, 'WATER');
insert into pokemon(name, number, type_main)
	values('Magikarp', 129, 'WATER');
insert into pokemon(name, number, type_main)
	values('Psyduck', 54, 'WATER');
    
# Sample Queries
select * from trainer;
select * from pokemon;