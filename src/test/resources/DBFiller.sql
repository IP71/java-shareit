delete from comments cascade;
delete from bookings cascade;
delete from items cascade;
delete from requests cascade;
delete from users cascade;
insert into users(id, name, email) values
(1, 'user1', 'user1@email.com'),
(2, 'user2', 'user2@email.ru'),
(3, 'user3', 'user3@mail.ru');
insert into requests(id, description, requestor_id, creation_date) values
(1, 'request1', 2, '2023-10-30 20.00.00'),
(2, 'request2', 2, '2023-10-30 21.00.00'),
(3, 'request3', 3, '2023-11-01 10.00.00');
insert into items(id, name, description, is_available, owner_id, request_id) values
(1, 'item1', 'desc1', true, 1, null),
(2, 'item2', 'desc2', false, 1, 1),
(3, 'item3', 'desc3', true, 2, null);
insert into bookings(id, start_date, end_date, item_id, booker_id, status) values
(1, '2023-10-30 20.00.00', '2024-10-30 20.00.00', 1, 2, 'WAITING'),
(2, '2023-11-30 20.00.00', '2023-12-30 20.00.00', 3, 1, 'WAITING'),
(3, '2023-12-30 20.00.00', '2024-01-30 20.00.00', 3, 3, 'WAITING');
insert into comments(id, text, item_id, author_id, creation_date) values
(1, 'comment1', 1, 2, '2023-11-30 20.00.00'),
(2, 'comment2', 3, 3, '2024-10-30 20.00.00');
