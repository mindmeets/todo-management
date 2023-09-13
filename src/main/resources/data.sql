insert into todo(id, username, description, target_date, done)
values
(10001, 'user', 'Learn JPA', CURRENT_TIMESTAMP(), false),
(10002, 'user', 'Get AWS certified', CURRENT_TIMESTAMP(), false),
(10003, 'user', 'Learn React', CURRENT_TIMESTAMP(), true);

--CURRENT_DATE, CURRENT_TIMESTAMP, or LOCALTIMESTAMP in H2