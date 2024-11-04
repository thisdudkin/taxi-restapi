CREATE TABLE IF NOT EXISTS drivers
(
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(128)                                       NOT NULL UNIQUE,
    email       VARCHAR(128)                                       NOT NULL UNIQUE,
    password    VARCHAR(128)                                       NOT NULL,
    firstname   VARCHAR(128)                                       NOT NULL,
    lastname    VARCHAR(128)                                       NOT NULL,
    phone       VARCHAR(128)                                       NOT NULL,
    birthdate   DATE CHECK (birthdate <= CURRENT_DATE),
    balance     DECIMAL(8, 2),
    status      VARCHAR(64)                                        NOT NULL,
    experience  INTEGER,
    created_utc TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_utc TIMESTAMP WITH TIME ZONE                           NULL
);

CREATE INDEX IF NOT EXISTS idx_drivers_username ON drivers USING btree (username);

CREATE TABLE IF NOT EXISTS driver_ratings
(
    driver_id BIGINT NOT NULL,
    rating    INTEGER,

    FOREIGN KEY (driver_id) REFERENCES drivers (id)
);

CREATE TABLE IF NOT EXISTS cars
(
    id            BIGSERIAL PRIMARY KEY,
    license_plate VARCHAR(128)                                       NOT NULL UNIQUE,
    model         VARCHAR(128)                                       NOT NULL,
    type          VARCHAR(64)                                        NOT NULL,
    year          INT                                                NOT NULL,
    color         VARCHAR(64)                                        NOT NULL,
    created_utc   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_utc   TIMESTAMP WITH TIME ZONE                           NULL
);

CREATE INDEX IF NOT EXISTS idx_cars_license_plate ON cars USING btree (license_plate);

CREATE TABLE IF NOT EXISTS driver_car_assignments
(
    id              BIGSERIAL PRIMARY KEY,
    driver_id       BIGINT                                             NOT NULL,
    car_id          BIGINT                                             NOT NULL,
    assignment_date TIMESTAMP WITH TIME ZONE                           NOT NULL,
    status          VARCHAR(64)                                        NOT NULL,
    created_utc     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_utc     TIMESTAMP WITH TIME ZONE                           NULL,

    FOREIGN KEY (driver_id) REFERENCES drivers (id) ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_assignments_driver_car ON driver_car_assignments (driver_id, car_id);
CREATE INDEX IF NOT EXISTS idx_assignments_status ON driver_car_assignments USING btree (status);