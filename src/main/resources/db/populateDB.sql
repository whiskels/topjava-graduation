DELETE FROM user_roles;
DELETE FROM votes;
DELETE FROM users;
DELETE FROM restaurants;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO restaurants (name)
VALUES ('Corner Grill'),
       ('Bluefin'),
       ('McDonalds'),
       ('Caesars Palace');

INSERT INTO votes (user_id, restaurant_id, date)
VALUES (100000, 100002, '2020-08-10'),
       (100000, 100002, '2020-08-11'),
       (100000, 100004, '2020-08-12'),
       (100001, 100005, '2020-08-10'),
       (100001, 100003, '2020-08-11'),
       (100001, 100005, '2020-08-12'),
       (100001, 100002, '2020-08-13')
