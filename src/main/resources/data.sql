INSERT INTO category(name, parent_id) VALUES('뮤지컬', NULL);
INSERT INTO category(name, parent_id) VALUES('스포츠', NULL);
INSERT INTO category(name, parent_id) VALUES('야구', 2);
INSERT INTO category(name, parent_id) VALUES('축구', 2);
INSERT INTO category(name, parent_id) VALUES('배구', 2);
INSERT INTO category(name, parent_id) VALUES('연극', NULL);
INSERT INTO category(name, parent_id) VALUES('전시', NULL);

insert into place (id, name, location, create_at, update_at) values(1, '잠실경기장', '서울특별시 잠실', now(), now());
insert into seat(id, section, seat_num, place_id, create_at, update_at) values(1, 'A', 1, 1, now(), now());
insert into seat(id, section, seat_num, place_id, create_at, update_at) values(2, 'A', 2, 1, now(), now());
insert into seat(id, section, seat_num, place_id, create_at, update_at) values(3, 'A', 3, 1, now(), now());
insert into seat(id, section, seat_num, place_id, create_at, update_at) values(4, 'A', 4, 1, now(), now());
insert into seat(id, section, seat_num, place_id, create_at, update_at) values(5, 'A', 5, 1, now(), now());
insert into seat(id, section, seat_num, place_id, create_at, update_at) values(6, 'B', 1, 1, now(), now());
insert into seat(id, section, seat_num, place_id, create_at, update_at) values(7, 'B', 2, 1, now(), now());
insert into seat(id, section, seat_num, place_id, create_at, update_at) values(8, 'B', 3, 1, now(), now());
insert into seat(id, section, seat_num, place_id, create_at, update_at) values(9, 'B', 4, 1, now(), now());