TRUNCATE TABLE passenger_ratings RESTART IDENTITY CASCADE;
TRUNCATE TABLE passengers RESTART IDENTITY CASCADE;

-- test data
INSERT INTO passengers (id, username, firstname, lastname, phone, birthdate, payment_method, created_at, updated_at)
VALUES
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6981', 'username1', 'John', 'Doe', '1234567890', '1985-05-15', 'CREDIT_CARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6982', 'username2', 'Jane', 'Smith', '0987654321', '1990-08-20', 'CASH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6983', 'username3', 'Alice', 'Johnson', '5555555555', '1982-12-30', 'CREDIT_CARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6984', 'username4', 'Bob', 'Brown', '4444444444', '1975-03-25', 'CASH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6985', 'username5', 'Charlie', 'Davis', '3333333333', '1995-07-10', 'CREDIT_CARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6986', 'username6', 'Eve', 'Miller', '2222222222', '1988-11-11', 'CASH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6987', 'username7', 'Frank', 'Wilson', '6666666666', '1970-01-01', 'CREDIT_CARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6988', 'username8', 'Grace', 'Taylor', '7777777777', '1992-06-06', 'CASH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6989', 'username9', 'Hank', 'Anderson', '8888888888', '1980-09-09', 'CREDIT_CARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('862eb8bc-8d7e-4a44-9dd2-cc258faf6980', 'username10', 'Ivy', 'Thomas', '9999999999', '1993-04-04', 'CASH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

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
