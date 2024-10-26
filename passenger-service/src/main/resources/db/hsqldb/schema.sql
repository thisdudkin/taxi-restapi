DROP TABLE IF EXISTS passengers_rating;
DROP TABLE IF EXISTS passengers;

CREATE TABLE IF NOT EXISTS passengers
(
    id             BIGINT IDENTITY PRIMARY KEY,
    username       VARCHAR(128)                        NOT NULL UNIQUE,
    email          VARCHAR(128)                        NOT NULL UNIQUE,
    password       VARCHAR(128)                        NOT NULL,
    firstname      VARCHAR(128)                        NOT NULL,
    lastname       VARCHAR(128)                        NOT NULL,
    phone          VARCHAR(128)                        NOT NULL,
    birthdate      DATE,
    payment_method VARCHAR(30)                         NOT NULL,
    balance        DECIMAL(6, 2),
    created_utc    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_utc    TIMESTAMP                           NULL
);

CREATE INDEX IF NOT EXISTS idx_passengers_username ON passengers (username);

CREATE TABLE IF NOT EXISTS passengers_rating
(
    passenger_id BIGINT NOT NULL,
    rating       INTEGER,
    FOREIGN KEY (passenger_id) REFERENCES passengers (id)
);
