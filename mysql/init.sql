-- Create tables ------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
                                     id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    username    VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255),
    is_active   BIT
    );

CREATE TABLE IF NOT EXISTS training_type (
                                             id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
                                             training_type_name  VARCHAR(255) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS trainees (
                                        id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        date_of_birth DATE,
                                        address       VARCHAR(255),
    user_id       BIGINT UNIQUE NOT NULL,
    CONSTRAINT fk_trainees_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS trainers (
                                        id               BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        specialization_id BIGINT NOT NULL,
                                        user_id          BIGINT UNIQUE NOT NULL,
                                        CONSTRAINT fk_trainers_user  FOREIGN KEY (user_id)  REFERENCES users(id),
    CONSTRAINT fk_trainers_spec  FOREIGN KEY (specialization_id) REFERENCES training_type(id)
    );

CREATE TABLE IF NOT EXISTS trainee_trainer (
                                               trainee_id BIGINT NOT NULL,
                                               trainer_id BIGINT NOT NULL,
                                               PRIMARY KEY (trainee_id, trainer_id),
    FOREIGN KEY (trainee_id) REFERENCES trainees(id),
    FOREIGN KEY (trainer_id) REFERENCES trainers(id)
    );

CREATE TABLE IF NOT EXISTS trainings (
                                         id               BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         trainee_id       BIGINT NOT NULL,
                                         trainer_id       BIGINT NOT NULL,
                                         training_type_id BIGINT NOT NULL,
                                         training_name    VARCHAR(255),
    training_date    DATE,
    training_duration INT,
    FOREIGN KEY (trainee_id)       REFERENCES trainees(id),
    FOREIGN KEY (trainer_id)       REFERENCES trainers(id),
    FOREIGN KEY (training_type_id) REFERENCES training_type(id)
    );

-- Seed lookup table --------------------------------------------------
INSERT IGNORE INTO training_type (training_type_name) VALUES
  ('AEROBICS'), ('CARDIO'), ('STRENGTH'), ('YOGA'),
  ('PILATES'), ('CROSSFIT'), ('SPINNING'), ('ZUMBA'),
  ('BODYBUILDING'), ('BALANCE'), ('DANCING'),
  ('BOXING'), ('MEDITATION'), ('OTHER');

-- Tiny demo dataset --------------------------------------------------
INSERT IGNORE INTO users (id, first_name, last_name, username, password, is_active)
VALUES (1,'Alice','Trainer','alice.trainer','secret',1),
       (2,'Bob',  'Trainee','bob.trainee','secret',1);

INSERT IGNORE INTO trainees (id, date_of_birth, address, user_id)
VALUES (1, '1996-03-14', '123 Main St', 2);

INSERT IGNORE INTO trainers (id, specialization_id, user_id)
VALUES (1, 4 /*YOGA*/, 1);

INSERT IGNORE INTO trainee_trainer (trainee_id, trainer_id) VALUES (1,1);
