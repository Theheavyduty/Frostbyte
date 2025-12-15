-- Departments
INSERT INTO departments (name) VALUES
    ('Sommerfulg'),
    ('Regnbue'),
    ('Skog'),
    ('Byen');

-- Employees (passwords are BCrypt hash of 'password123')
-- The password does not work in the real website/testside since its not been salted(just hashed)
INSERT INTO employees (name, email, password, phone_number, address, profile_picture_url) VALUES
    ('Anna Jensen', 'anna.jensen@frostbyte.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjqQBrkAb/T.0BZXa3l0VftFhNqzYu', 12345678, '123 Main Street', NULL),
    ('Erik Larsen', 'erik.larsen@frostbyte.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjqQBrkAb/T.0BZXa3l0VftFhNqzYu', 87654321, '456 Oak Avenue', NULL),
    ('Maria Hansen', 'maria.hansen@frostbyte.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjqQBrkAb/T.0BZXa3l0VftFhNqzYu', 11223344, '789 Pine Road', NULL),
    ('Lars Olsen', 'lars.olsen@frostbyte.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjqQBrkAb/T.0BZXa3l0VftFhNqzYu', 55667788, '321 Birch Lane', NULL);

INSERT INTO employee_departments (employee_id, department_id) VALUES
    (1, 1),
    (1, 2),
    (2, 2),
    (2, 3),
    (3, 3),
    (3, 4),
    (4, 1),
    (4, 4);

INSERT INTO parents (name, email, phone_number, address, profile_picture_url) VALUES
    ('Peter Nielsen', 'peter.nielsen@email.com', 99887766, '100 Family Street', NULL),
    ('Sara Andersen', 'sara.andersen@email.com', 77665544, '200 Parent Avenue', NULL),
    ('Thomas Berg', 'thomas.berg@email.com', 33445566, '300 Guardian Road', NULL),
    ('Lisa Holm', 'lisa.holm@email.com', 22334455, '400 Caretaker Lane', NULL);

INSERT INTO children (name, email, password, phone_number, address, department_id, birthday, profile_picture_url, additional_notes) VALUES
    ('Emma Nielsen', 'emma.nielsen@email.com', NULL, 11112222, '100 Family Street', 1, '2022-03-15', NULL, 'Allergic to peanuts'),
    ('Oscar Nielsen', 'oscar.nielsen@email.com', NULL, 11113333, '100 Family Street', 2, '2020-07-22', NULL, 'Loves dinosaurs'),
    ('Ida Andersen', 'ida.andersen@email.com', NULL, 22223333, '200 Parent Avenue', 2, '2020-11-08', NULL, NULL),
    ('Noah Andersen', 'noah.andersen@email.com', NULL, 22224444, '200 Parent Avenue', 3, '2019-01-30', NULL, 'Needs glasses for reading'),
    ('Freja Berg', 'freja.berg@email.com', NULL, 33334444, '300 Guardian Road', 1, '2023-02-14', NULL, 'Naps at 1pm'),
    ('William Holm', 'william.holm@email.com', NULL, 44445555, '400 Caretaker Lane', 3, '2018-09-05', NULL, NULL),
    ('Alma Holm', 'alma.holm@email.com', NULL, 44446666, '400 Caretaker Lane', 4, '2016-12-20', NULL, 'Picked up by grandma on Fridays');


INSERT INTO parent_children (parent_id, child_id, registration_number) VALUES
    (1, 1, 1),
    (1, 2, 1),
    (2, 3, 1),
    (2, 4, 1),
    (3, 5, 1),
    (4, 6, 1),
    (4, 7, 1);

INSERT INTO parent_children (parent_id, child_id, registration_number) VALUES
    (2, 1, 2),
    (3, 4, 2),
    (1, 7, 2);

-- Child Status Logs (sample check-in/check-out data)
INSERT INTO child_status_logs (child_id, status, event_time, symptoms, absence_reason, registered_by_employee_id) VALUES
    (1, 'LEVERT', '2025-01-13 07:45:00', NULL, NULL, 1),
    (1, 'HENTET', '2025-01-13 16:30:00', NULL, NULL, 4),
    (1, 'LEVERT', '2025-01-14 08:00:00', NULL, NULL, 1),

    (2, 'LEVERT', '2025-01-13 08:15:00', NULL, NULL, 2),
    (2, 'HENTET', '2025-01-13 15:45:00', NULL, NULL, 2),
    (2, 'SYK', '2025-01-14 09:00:00', 'Feber og hoste', NULL, 1),

    (3, 'LEVERT', '2025-01-13 07:30:00', NULL, NULL, 2),
    (3, 'HENTET', '2025-01-13 16:00:00', NULL, NULL, 2),
    (3, 'FRAVAER', '2025-01-14 08:00:00', NULL, 'Familiebesøk', 2),

    (4, 'LEVERT', '2025-01-13 08:00:00', NULL, NULL, 3),
    (4, 'HENTET', '2025-01-13 17:00:00', NULL, NULL, 3),
    (4, 'LEVERT', '2025-01-14 07:55:00', NULL, NULL, 3),

    (5, 'LEVERT', '2025-01-13 08:30:00', NULL, NULL, 4),
    (5, 'HENTET', '2025-01-13 14:00:00', NULL, NULL, 1),
    (5, 'LEVERT', '2025-01-14 08:45:00', NULL, NULL, 4),

    (6, 'LEVERT', '2025-01-13 07:50:00', NULL, NULL, 3),
    (6, 'SYK', '2025-01-13 11:30:00', 'Mageknip og kvalm', NULL, 3),

    (7, 'LEVERT', '2025-01-13 14:00:00', NULL, NULL, 3),
    (7, 'HENTET', '2025-01-13 17:30:00', NULL, NULL, 3),
    (7, 'LEVERT', '2025-01-14 14:15:00', NULL, NULL, 3);