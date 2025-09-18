-- Add this at the top of your init.sql
SELECT 'Starting database initialization...' as status;

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
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    specialization_id BIGINT NOT NULL,
    user_id           BIGINT UNIQUE NOT NULL,
    CONSTRAINT fk_trainers_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_trainers_spec FOREIGN KEY (specialization_id) REFERENCES training_type(id)
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
    active_session    BIT,
    FOREIGN KEY (trainee_id)       REFERENCES trainees(id),
    FOREIGN KEY (trainer_id)       REFERENCES trainers(id),
    FOREIGN KEY (training_type_id) REFERENCES training_type(id)
    );

CREATE TABLE IF NOT EXISTS revoked_token (
    token       VARCHAR(512) PRIMARY KEY,
    expires_at  TIMESTAMP NOT NULL
    );

-- Add this before your INSERT statements
SELECT 'Tables created, starting seed data insertion...' as status;

-- Insert seed values

INSERT IGNORE INTO training_type (training_type_name) VALUES
  ('AEROBICS'),
  ('CARDIO'),
  ('STRENGTH'),
  ('YOGA'),
  ('PILATES'),
  ('CROSSFIT'),
  ('SPINNING'),
  ('ZUMBA'),
  ('BODYBUILDING'),
  ('BALANCE'),
  ('DANCING'),
  ('BOXING'),
  ('MEDITATION'),
  ('OTHER');

-- Add this at the end
SELECT 'Database initialization completed!' as status;
SELECT COUNT(*) as training_types_inserted FROM training_type;
