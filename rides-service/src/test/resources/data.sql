TRUNCATE TABLE rides RESTART IDENTITY;

-- test data
INSERT INTO rides (passenger, driver, car, status, from_county, from_city, from_street, to_county, to_city, to_street, price, payment_method, start_time, end_time, created_at, updated_at)
VALUES
    (1, 101, 1001, 'ACTIVE', 'Orange County', 'Los Angeles', 'Sunset Blvd', 'Orange County', 'Irvine', 'University Dr', 1000.00,'CREDIT_CARD', '2023-11-01 10:00:00', '2023-11-01 10:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 102, 1002, 'DONE', 'San Diego County', 'San Diego', 'Market St', 'Orange County', 'Anaheim', 'Harbor Blvd', 1000.00, 'CASH', '2023-11-02 11:00:00', '2023-11-02 11:45:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 103, 1003, 'CANCEL', 'Orange County', 'Santa Ana', 'Main St', 'Orange County', 'Fullerton', 'Valencia Dr', 1000.00, 'CREDIT_CARD', '2023-11-03 12:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 104, 1004, 'DONE', 'Riverside County', 'Riverside', 'Magnolia Ave', 'Los Angeles County', 'Pasadena', 'Colorado Blvd', 1000.00, 'CASH', '2023-11-04 13:00:00', '2023-11-04 13:35:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 105, 1005, 'ACTIVE', 'Los Angeles County', 'Long Beach', 'Ocean Blvd', 'San Diego County', 'La Jolla', 'Torrey Pines Rd', 1000.00, 'CREDIT_CARD', '2023-11-05 14:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (6, 106, 1006, 'DONE', 'Orange County', 'Huntington Beach', 'Beach Blvd', 'Los Angeles County', 'Los Angeles', 'Wilshire Blvd', 1000.00, 'CASH', '2023-11-06 15:00:00', '2023-11-06 15:40:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (7, 107, 1007, 'CANCEL', 'San Bernardino County', 'San Bernardino', 'D St', 'Orange County', 'Irvine', 'Alton Pkwy', 1000.00, 'CREDIT_CARD', '2023-11-07 16:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (8, 108, 1008, 'DONE', 'Los Angeles County', 'Burbank', 'Olive Ave', 'Riverside County', 'Corona', 'Main St', 1000.00, 'CASH', '2023-11-08 17:00:00', '2023-11-08 17:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (9, 109, 1009, 'ACTIVE', 'Orange County', 'Newport Beach', 'Balboa Blvd', 'Los Angeles County', 'Hollywood', 'Hollywood Blvd', 1000.00, 'CREDIT_CARD', '2023-11-09 18:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10, 110, 1010, 'DONE', 'San Diego County', 'Chula Vista', '3rd Ave', 'San Bernardino County', 'Ontario', 'Mountain Ave', 1000.00, 'CASH', '2023-11-10 19:00:00', '2023-11-10 19:50:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
