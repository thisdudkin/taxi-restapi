TRUNCATE TABLE rides RESTART IDENTITY;

-- test data
INSERT INTO rides (
    passenger,
    driver,
    car,
    status,
    from_country,
    from_city,
    from_street,
    from_lat,
    from_lng,
    to_country,
    to_city,
    to_street,
    to_lat,
    to_lng,
    price,
    payment_method,
    start_time,
    end_time,
    created_at,
    updated_at
) VALUES
      (1, 101, 1001, 'PENDING', 'Orange County', 'Los Angeles', 'Sunset Blvd', 34.0522, -118.2437, 'Orange County', 'Irvine', 'University Dr', 33.6846, -117.8265, 1000.00, 'CREDIT_CARD', '2023-11-01 10:00:00', '2023-11-01 10:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (2, 102, 1002, 'ASSIGNED', 'San Diego County', 'San Diego', 'Market St', 32.7157, -117.1611, 'Orange County', 'Anaheim', 'Harbor Blvd', 33.8003, -117.9190, 1000.00, 'CASH', '2023-11-02 11:00:00', '2023-11-02 11:45:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (3, 103, 1003, 'ACTIVE', 'Orange County', 'Santa Ana', 'Main St', 33.7455, -117.8677, 'Orange County', 'Fullerton', 'Valencia Dr', 33.8705, -117.9258, 1000.00, 'CREDIT_CARD', '2023-11-03 12:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (4, 104, 1004, 'ACTIVE', 'Riverside County', 'Riverside', 'Magnolia Ave', 33.9533, -117.3962, 'Los Angeles County', 'Pasadena', 'Colorado Blvd', 34.1478, -118.1445, 1000.00, 'CASH', '2023-11-04 13:00:00', '2023-11-04 13:35:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (5, 105, 1005, 'ACTIVE', 'Los Angeles County', 'Long Beach', 'Ocean Blvd', 33.7701, -118.1937, 'San Diego County', 'La Jolla', 'Torrey Pines Rd', 32.8736, -117.2712, 1000.00, 'CREDIT_CARD', '2023-11-05 14:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (6, 106, 1006, 'ACTIVE', 'Orange County', 'Huntington Beach', 'Beach Blvd', 33.6603, -117.9992, 'Los Angeles County', 'Los Angeles', 'Wilshire Blvd', 34.0696, -118.4495, 1000.00, 'CASH', '2023-11-06 15:00:00', '2023-11-06 15:40:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (7, 107, 1007, 'ACTIVE', 'San Bernardino County', 'San Bernardino', 'D St', 34.1083, -117.2898, 'Orange County', 'Irvine', 'Alton Pkwy', 33.6846, -117.8265, 1000.00, 'CREDIT_CARD', '2023-11-07 16:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (8, 108, 1008, 'ACTIVE', 'Los Angeles County', 'Burbank', 'Olive Ave', 34.1808, -118.3083, 'Riverside County', 'Corona', 'Main St', 33.8753, -117.5644, 1000.00, 'CASH', '2023-11-08 17:00:00', '2023-11-08 17:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (9, 109, 1009, 'ACTIVE', 'Orange County', 'Newport Beach', 'Balboa Blvd', 33.6189, -117.9280, 'Los Angeles County', 'Hollywood', 'Hollywood Blvd', 34.0928, -118.3287, 1000.00, 'CREDIT_CARD', '2023-11-09 18:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (10, 110, 1010, 'ACTIVE', 'San Diego County', 'Chula Vista', '3rd Ave', 32.6401, -117.0841, 'San Bernardino County', 'Ontario', 'Mountain Ave', 34.0633, -117.6509, 1000.00, 'CASH', '2023-11-10 19:00:00', '2023-11-10 19:50:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
