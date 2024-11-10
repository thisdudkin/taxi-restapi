TRUNCATE TABLE rides RESTART IDENTITY;

INSERT INTO rides (passenger, driver, car, status, from_county, from_city, from_street, to_county, to_city, to_street, payment_method, start_time, end_time, created_utc, price)
VALUES (1, 1, 101, 'ACTIVE', 'CountyA', 'CityA', 'StreetA', 'CountyB', 'CityB', 'StreetB', 'CASH',
        '2024-11-10 10:00:00+00', '2024-11-10 11:00:00+00', CURRENT_TIMESTAMP, 15.50),
       (2, 2, 102, 'DONE', 'CountyC', 'CityC', 'StreetC', 'CountyD', 'CityD', 'StreetD', 'CASH',
        '2024-11-11 12:00:00+00', NULL, CURRENT_TIMESTAMP, 20.00),
       (3, 3, 103, 'DONE', 'CountyE', 'CityE', 'StreetE', 'CountyF', 'CityF', 'StreetF', 'CASH',
        '2024-11-12 14:00:00+00', '2024-11-12 14:30:00+00', CURRENT_TIMESTAMP, 25.75),
       (1, 2, 101, 'ACTIVE', 'CountyA', 'CityA', 'StreetA', 'CountyG', 'CityG', 'StreetG', 'CREDIT_CARD',
        '2024-11-13 15:00:00+00', NULL, CURRENT_TIMESTAMP, 30.00),
       (2, 1, 102, 'CANCEL', 'CountyB', 'CityB', 'StreetB', 'CountyH', 'CityH', 'StreetH', 'CREDIT_CARD',
        '2024-11-14 16:00:00+00', '2024-11-14 16:30:00+00', CURRENT_TIMESTAMP, 0.00),
       (3, 3, 103, 'CANCEL', 'CountyC', 'CityC', 'StreetC', 'CountyI', 'CityI', 'StreetI', 'CREDIT_CARD',
        '2024-11-15 17:00:00+00', NULL, CURRENT_TIMESTAMP, 45.20),
       (1, 2, 101, 'CANCEL', 'CountyD', 'CityD', 'StreetD', 'CountyJ', 'CityJ', 'StreetJ', 'CASH',
        '2024-11-16 18:00:00+00', NULL, CURRENT_TIMESTAMP, 55.10);
