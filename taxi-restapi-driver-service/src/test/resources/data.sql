TRUNCATE TABLE driver_ratings RESTART IDENTITY CASCADE;
TRUNCATE TABLE driver_car_assignments RESTART IDENTITY CASCADE;
TRUNCATE TABLE cars RESTART IDENTITY CASCADE;
TRUNCATE TABLE drivers RESTART IDENTITY CASCADE;

-- test data
INSERT INTO drivers (id, firstname, lastname, phone, birthdate, balance, status, experience,
                     created_at, updated_at)
VALUES (100, 'John', 'Doe', '+1234567890', '1990-01-15', 1000.50, 'READY', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (101, 'Jane', 'Smith', '+1234567891', '1985-05-20', 1500.00, 'OFFLINE', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (102, 'Alice', 'Johnson', '+1234567892', '1978-08-30', 750.75, 'OFFLINE', 10, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (103, 'Bob', 'Brown', '+1234567893', '1992-11-02', 2000.25, 'READY', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (104, 'Charlie', 'Davis', '+1234567894', '2000-03-18', 500.00, 'READY', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO cars (id, license_plate, model, type, year, color)
VALUES (100, 'ABC123', 'Toyota Prius', 'PREMIUM', 2020, 'Blue'),
       (101, 'XYZ789', 'Ford F-150', 'PREMIUM', 2019, 'Red'),
       (102, 'LMN456', 'Honda Civic', 'PREMIUM', 2021, 'Black'),
       (103, 'TRE762', 'Nissan Skyline', 'PREMIUM', 2001, 'White'),
       (104, 'ISD341', 'Jeep', 'PREMIUM', 2011, 'Black'),
       (105, 'GJW235', 'Honda S2000', 'PREMIUM', 1999, 'Pink'),
       (106, 'UYV315', 'Golf', 'PREMIUM', 2017, 'Yellow');

INSERT INTO driver_car_assignments (id, driver_id, car_id, assignment_date, status)
VALUES (100, 100, 100, CURRENT_TIMESTAMP, 'ACTIVE'),
       (101, 101, 101, CURRENT_TIMESTAMP, 'ACTIVE'),
       (102, 102, 102, CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO driver_ratings (driver_id, rating)
VALUES (100, 5),
       (101, 4),
       (102, 3);
