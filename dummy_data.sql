-- Insert sample Trainer data
INSERT INTO trainer (username, password, role, enabled, email)
VALUES ('trainer1', 'password1', 'ROLE_USER', true, 'trainer1@example.com');

INSERT INTO trainer (username, password, role, enabled, email)
VALUES ('trainer2', 'password2', 'ROLE_USER', true, 'trainer2@example.com');

-- Insert sample Pokemon data
INSERT INTO pokemon (name, number, type)
VALUES ('Pikachu', 25, 'ELECTRIC');

INSERT INTO pokemon (name, number, type)
VALUES ('Charmander', 4, 'FIRE');

INSERT INTO pokemon (name, number, type)
VALUES ('Squirtle', 7, 'WATER');

-- Insert sample Team data
INSERT INTO team (trainer_id, pokemon_id)
VALUES (1, 1); -- Assigns Pikachu to trainer1

INSERT INTO team (trainer_id, pokemon_id)
VALUES (2, 2); -- Assigns Charmander to trainer2

INSERT INTO team (trainer_id, pokemon_id)
VALUES (1, 3); -- Assigns Squirtle to trainer1

select * from trainer;