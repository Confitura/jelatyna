INSERT INTO participant (id, first_Name, last_Name, email) VALUES (1, 'test', 'testowy', 'admin@admin.pl'),(2, 'test', 'testowy', 'test@admin.pl');
INSERT INTO users (id, participant_id, password) VALUES (1, 1, 'admin@admin.pl'), (2, 2, 'test@admin.pl');

INSERT INTO user_roles (user_id, roles) VALUES (1, 'ADMIN');
