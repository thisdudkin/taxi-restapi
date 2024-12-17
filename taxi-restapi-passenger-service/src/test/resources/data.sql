TRUNCATE TABLE passenger_ratings RESTART IDENTITY CASCADE;
TRUNCATE TABLE passengers RESTART IDENTITY CASCADE;

-- test data
INSERT INTO passengers (id, firstname, lastname, phone, birthdate, payment_method, balance, created_at, updated_at)
VALUES
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6981', 'John', 'Doe', '1234567890', '1985-05-15', 'CREDIT_CARD', 100.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6982', 'Jane', 'Smith', '0987654321', '1990-08-20', 'CASH', 200.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6983', 'Alice', 'Johnson', '5555555555', '1982-12-30', 'CREDIT_CARD', 150.75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6984', 'Bob', 'Brown', '4444444444', '1975-03-25', 'CASH', 300.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6985', 'Charlie', 'Davis', '3333333333', '1995-07-10', 'CREDIT_CARD', 50.25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6986', 'Eve', 'Miller', '2222222222', '1988-11-11', 'CASH', 500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6987', 'Frank', 'Wilson', '6666666666', '1970-01-01', 'CREDIT_CARD', 250.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6988', 'Grace', 'Taylor', '7777777777', '1992-06-06', 'CASH', 175.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6989', 'Hank', 'Anderson', '8888888888', '1980-09-09', 'CREDIT_CARD', 300.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6980', 'Ivy', 'Thomas', '9999999999', '1993-04-04', 'CASH', 125.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO passenger_ratings (id, passenger_id, rating, created_at)
VALUES
    (gen_random_uuid(), '862eb8bc-8d7e-4a44-9dd2-cc258faf6981', 5, CURRENT_TIMESTAMP),
    (gen_random_uuid(), '862eb8bc-8d7e-4a44-9dd2-cc258faf6982', 4, CURRENT_TIMESTAMP),
    (gen_random_uuid(), '862eb8bc-8d7e-4a44-9dd2-cc258faf6983', 3, CURRENT_TIMESTAMP),
    (gen_random_uuid(), '862eb8bc-8d7e-4a44-9dd2-cc258faf6984', 5, CURRENT_TIMESTAMP),
    (gen_random_uuid(), '862eb8bc-8d7e-4a44-9dd2-cc258faf6985', 2, CURRENT_TIMESTAMP),
    (gen_random_uuid(), '862eb8bc-8d7e-4a44-9dd2-cc258faf6986', 4, CURRENT_TIMESTAMP),
    (gen_random_uuid(), '862eb8bc-8d7e-4a44-9dd2-cc258faf6987', 5, CURRENT_TIMESTAMP),
    (gen_random_uuid(), '862eb8bc-8d7e-4a44-9dd2-cc258faf6988', 3, CURRENT_TIMESTAMP),
    (gen_random_uuid(), '862eb8bc-8d7e-4a44-9dd2-cc258faf6989', 4, CURRENT_TIMESTAMP),
    (gen_random_uuid(), '862eb8bc-8d7e-4a44-9dd2-cc258faf6980', 5, CURRENT_TIMESTAMP);
