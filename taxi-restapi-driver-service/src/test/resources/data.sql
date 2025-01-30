TRUNCATE TABLE driver_ratings RESTART IDENTITY CASCADE;
TRUNCATE TABLE driver_car_assignments RESTART IDENTITY CASCADE;
TRUNCATE TABLE cars RESTART IDENTITY CASCADE;
TRUNCATE TABLE drivers RESTART IDENTITY CASCADE;

-- test data
INSERT INTO drivers (id, username, firstname, lastname, phone, birthdate, status, experience,
                     created_at, updated_at)
VALUES ('862eb8bc-8d7e-4a44-9dd2-cc258faf6981', 'username1', 'John', 'Doe', '+1234567890', '1990-01-15', 'READY', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6982', 'username2', 'Jane', 'Smith', '+1234567891', '1985-05-20', 'OFFLINE', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6983', 'username3', 'Alice', 'Johnson', '+1234567892', '1978-08-30', 'OFFLINE', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6984', 'username4', 'Bob', 'Brown', '+1234567893', '1992-11-02', 'READY', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6985', 'username5', 'Charlie', 'Davis', '+1234567894', '2000-03-18', 'READY', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO cars (id, license_plate, model, type, year, color)
VALUES ('862eb8bc-8d7e-4a44-9dd2-cc258faf6981', 'ABC123', 'Toyota Prius', 'PREMIUM', 2020, 'Blue'),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6982', 'XYZ789', 'Ford F-150', 'PREMIUM', 2019, 'Red'),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6983', 'LMN456', 'Honda Civic', 'PREMIUM', 2021, 'Black'),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6984', 'TRE762', 'Nissan Skyline', 'PREMIUM', 2001, 'White'),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6985', 'ISD341', 'Jeep', 'PREMIUM', 2011, 'Black'),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6986', 'GJW235', 'Honda S2000', 'PREMIUM', 1999, 'Pink'),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6987', 'UYV315', 'Golf', 'PREMIUM', 2017, 'Yellow');

INSERT INTO driver_car_assignments (id, driver_id, car_id, assignment_date, status)
VALUES ('862eb8bc-8d7e-4a44-9dd2-cc258faf6981', '862eb8bc-8d7e-4a44-9dd2-cc258faf6981', '862eb8bc-8d7e-4a44-9dd2-cc258faf6981', CURRENT_TIMESTAMP, 'ACTIVE'),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6982', '862eb8bc-8d7e-4a44-9dd2-cc258faf6982', '862eb8bc-8d7e-4a44-9dd2-cc258faf6982', CURRENT_TIMESTAMP, 'ACTIVE'),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6983', '862eb8bc-8d7e-4a44-9dd2-cc258faf6983', '862eb8bc-8d7e-4a44-9dd2-cc258faf6983', CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO driver_ratings (driver_id, rating)
VALUES ('862eb8bc-8d7e-4a44-9dd2-cc258faf6981', 5),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6982', 4),
       ('862eb8bc-8d7e-4a44-9dd2-cc258faf6983', 3);
