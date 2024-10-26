INSERT INTO passengers (username, email, password, firstname, lastname, phone, birthdate, payment_method, balance)
VALUES ('ivan', 'ivan@gmail.com', 'password', 'Ivan', 'Ivanov', '+375 44 543-97-26', '2000-01-19', 'CASH', 2000.00);

INSERT INTO passengers (username, email, password, firstname, lastname, phone, birthdate, payment_method, balance)
VALUES ('petr', 'petr@gmail.com', 'password123', 'Petr', 'Petrov', '+375 29 123-45-67', '1995-05-10', 'CREDIT_CARD', 1500.50);

INSERT INTO passengers (username, email, password, firstname, lastname, phone, birthdate, payment_method, balance)
VALUES ('anna', 'anna@example.com', 'password321', 'Anna', 'Ivanova', '+375 33 987-65-43', '1992-03-22', 'CASH', 100.00);

INSERT INTO passengers_rating (passenger_id, rating) VALUES (0, 4);
INSERT INTO passengers_rating (passenger_id, rating) VALUES (0, 5);
INSERT INTO passengers_rating (passenger_id, rating) VALUES (1, 3);
INSERT INTO passengers_rating (passenger_id, rating) VALUES (2, 5);
