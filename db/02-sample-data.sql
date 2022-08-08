--thêm dữ liệu bảng labs
insert into labs(id, name) VALUES (1,'Phòng tin 1');
insert into labs(id, name) VALUES (2,'Phòng tin 2');
insert into labs(id, name) VALUES (3,'Phòng tin 3');
insert into labs(id, name) VALUES (4,'Phòng tin 4');
insert into labs(id, name) VALUES (5,'Phòng tin 5');
insert into labs(id, name) VALUES (6,'Thực hành vật lý 1');
insert into labs(id, name) VALUES (7,'Thực hành vật lý 2');
insert into labs(id, name) VALUES (8,'Thực hành vật lý 3');
insert into labs(id, name) VALUES (9,'Thực hành vật lý 4');
insert into labs(id, name) VALUES (10,'Thực hành vật lý 5');

-- dữ liệu bảng lớp học
insert into clazz(id, name) VALUES (1,'6A');
insert into clazz(id, name) VALUES (2,'6B');
insert into clazz(id, name) VALUES (3,'6C');
insert into clazz(id, name) VALUES (4,'6D');
insert into clazz(id, name) VALUES (5,'7A');
insert into clazz(id, name) VALUES (6,'7B');
insert into clazz(id, name) VALUES (7,'7C');
insert into clazz(id, name) VALUES (8,'7D');
insert into clazz(id, name) VALUES (9,'8A');
insert into clazz(id, name) VALUES (10,'8B');
insert into clazz(id, name) VALUES (11,'8C');
insert into clazz(id, name) VALUES (12,'8D');
insert into clazz(id, name) VALUES (13,'9A');
insert into clazz(id, name) VALUES (14,'9B');
insert into clazz(id, name) VALUES (15,'9C');
insert into clazz(id, name) VALUES (16,'9D');

-- dữ liệu bảng giáo viên
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (1,'Thơm',1,null,1);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (2,'Huyền',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (3,'Mai',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (4,'Bông',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (5,'Nhật',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (6,'N.Hương',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (7,'Chuyên',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (8,'Ly',0,null,2);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (9,'Tuyết',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (10,'H.Hà',0,null,9);

insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (11,'Nghiệp',0,1,10);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (12,'Tuấn',0,0,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (20,'H.Hoa',0,1,4);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (21,'Tuyên',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (22,'H.Hương',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (23,'T.Xuyến',0,1,3);

insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (24,'Long',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (25,'Phương',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (26,'Tâm',0,1,5);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (27,'N.Xuyến',0,1,7);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (28,'Hường',0,1,11);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (29,'T.Hoa',0,1,6);

insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (30,'Ngát',1,1,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (31,'Nhung',0,null,8);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (32,'HàHT',0,null,null);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (33,'Nguyệt',1,1,15);

insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (34,'L.Anh',1,1,12);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (35,'Hướng',1,1,14);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (36,'T.Hà',1,1,13);
insert into teachers(id, name,has_children,skip_days,head_clazz_id) VALUES (37,'Tân',1,1,16);


-- dữ liệu bảng subjects
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (1,'Toán',null,0,1,0,'Toán', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (2,'Tin',1,2,0,1,'Tin', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (3,'Anh',null,0,1,1,'Anh', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (4,'Sinh',null,0,1,1,'Sinh', 0);

insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (5,'Văn',null,0,1,0,'Văn', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (6,'Sử',null,0,0,1,'Sử', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (7,'Địa',null,0,1,1,'Địa', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (8,'Công nghệ',null,0,0,1,'Công nghệ', 0);

insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (9,'Thể dục',null,0,1,1,'Thể dục', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (10,'KHTN',null,0,0,1,'KHTN', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (11,'SH',null,0,0,0,'SH', 0);

insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (12,'Hóa',null,0,1,1,'Hóa', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (13,'Chào cờ',null,0,0,0,'Chào cờ', 0);

insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (14,'Nhạc',null,0,0,0,'Nhạc', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (15,'Mỹ Thuật',null,0,0,0,'Mỹ Thuật', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (16,'Lý',7,0,0,1,'Lý', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (17,'Công dân',null,0,0,0,'Công dân', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (18,'VănKT',null,2,1,0,'VănKT', 0);

insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (19,'Nghỉ',null,0,0,0,'Nghỉ', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (20,'ToánKT',null,0,1,0,'ToánKT', 0);
-- dữ liệu thời khóa biểu của các lớp

--6A
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (1, 1, 8, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (2, 2, 6, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (3, 3, 7, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (4, 4, 17, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (5, 5, 15, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (6, 6, 14, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (7, 7, 3, 1, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (8, 8, 10, 1, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (9, 9, 9, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (10, 10, 2, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (11, 11, 1, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (12, 4, 18, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (13, 4, 5, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (14, 4, 11, 1, 1);

--6B
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (15, 10, 2, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (16, 7, 3, 2, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (17, 4, 17, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (18, 1, 8, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (19, 6, 14, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (20, 9, 9, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (21, 5, 15, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (22, 3, 7, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (23, 2, 6, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (24, 4, 5, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (25, 20, 1, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (26, 8, 10, 2, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (27, 4, 18, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (28, 8, 11, 2, 1);

--6C
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (29, 21, 10, 3, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (30, 3, 7, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (31, 10, 2, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (32, 5, 15, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (33, 22, 8, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (34, 4, 17, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (35, 23, 5, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (36, 2, 6, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (37, 5, 14, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (38, 7, 3, 3, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (39, 9, 9, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (40, 20, 1, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (41, 23, 11, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (42, 23, 18, 3, 2);

--6D
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (43, 2, 6, 4, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (44, 22, 8, 4, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (45, 3, 7, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (46, 24, 9, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (47, 5, 15, 4, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (48, 25, 10, 4, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (49, 20, 1, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (50, 10, 2, 4, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (51, 7, 3, 4, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (52, 5, 14, 4, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (53, 4, 17, 4, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (54, 23, 18, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (55, 23, 5, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (56, 20, 11, 4, 1);

--7A
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (57, 26, 16, 5, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (58, 2, 6, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (59, 27, 3, 5, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (60, 5, 15, 5, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (61, 26, 1, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (62, 1, 8, 5, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (63, 9, 4, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (64, 22, 2, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (65, 6, 14, 5, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (66, 23, 17, 5, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (67, 28, 9, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (68, 3, 7, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (69, 29, 18, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (70, 29, 5, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (71, 26, 11, 5, 1);

--7B
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (72, 9, 4, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (73, 30, 3, 6, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (74, 2, 7, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (75, 5, 15, 6, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (76, 22, 2, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (77, 26, 16, 6, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (78, 23, 17, 6, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (79, 6, 14, 6, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (80, 2, 6, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (81, 26, 1, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (82, 1, 8, 6, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (83, 29, 5, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (84, 28, 9, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (85, 29, 18, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (86, 29, 11, 6, 1);

--7C
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (87, 27, 3, 7, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (88, 9, 4, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (89, 2, 7, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (90, 6, 14, 7, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (91, 2, 9, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (92, 2, 6, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (93, 5, 15, 7, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (94, 23, 17, 7, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (95, 1, 5, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (96, 22, 2, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (97, 1, 8, 7, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (98, 26, 16, 7, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (99, 1, 18, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (100, 31, 1, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (101, 27, 11, 7, 1);

--7D
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (102, 2, 6, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (103, 2, 7, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (104, 26, 16, 8, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (105, 1, 8, 8, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (106, 12, 18, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (107, 9, 4, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (108, 5, 15, 8, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (109, 30, 3, 8, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (110, 23, 17, 8, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (111, 9, 9, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (112, 5, 14, 8, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (113, 31, 1, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (114, 22, 2, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (115, 12, 5, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (116, 31, 11, 8, 1);

--8A
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (117, 25, 12, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (118, 1, 6, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (119, 22, 4, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (120, 24, 9, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (121, 32, 8, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (122, 27, 3, 9, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (123, 21, 16, 9, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (124, 6, 14, 9, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (125, 33, 7, 9, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (126, 5, 15, 9, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (127, 29, 17, 9, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (128, 28, 5, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (129, 10, 1, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (130, 28, 18, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (131, 10, 2, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (132, 10, 11, 9, 1);

--8B
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (133, 22, 8, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (134, 21, 16, 10, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (135, 22, 4, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (136, 3, 7, 10, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (137, 5, 15, 10, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (138, 25, 12, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (139, 6, 14, 10, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (140, 10, 2, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (141, 30, 3, 10, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (142, 29, 17, 10, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (143, 1, 6, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (144, 24, 9, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (145, 34, 5, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (146, 34, 18, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (147, 11, 1, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (148, 11, 11, 10, 1);

--8C
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (149, 33, 7, 11, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (150, 31, 8, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (151, 5, 15, 11, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (152, 22, 4, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (153, 21, 16, 11, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (154, 1, 6, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (155, 6, 14, 11, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (156, 28, 18, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (157, 35, 1, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (158, 25, 12, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (159, 29, 17, 11, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (160, 28, 5, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (161, 10, 2, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (162, 7, 3, 11, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (163, 24, 9, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (164, 28, 11, 11, 1);

--8D
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (165, 21, 16, 12, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (166, 30, 3, 12, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (167, 29, 17, 12, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (168, 1, 6, 12, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (169, 25, 12, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (170, 31, 8, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (171, 22, 4, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (172, 5, 15, 12, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (173, 24, 9, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (174, 3, 7, 12, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (175, 10, 2, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (176, 6, 14, 12, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (177, 34, 5, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (178, 36, 1, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (179, 34, 18, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (180, 34, 11, 12, 1);

--9A
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (181, 5, 15, 13, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (182, 8, 4, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (183, 34, 17, 13, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (184, 31, 16, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (185, 35, 8, 13, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (186, 25, 12, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (187, 23, 6, 13, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (188, 33, 5, 13, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (189, 36, 1, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (190, 7, 3, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (191, 3, 7, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (192, 24, 9, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (193, 11, 2, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (194, 33, 18, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (195, 36, 11, 13, 1);

--9B
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (196, 5, 15, 14, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (197, 35, 8, 14, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (198, 8, 4, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (199, 25, 12, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (200, 23, 6, 14, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (201, 21, 16, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (202, 30, 3, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (203, 34, 17, 14, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (204, 35, 1, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (205, 37, 18, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (206, 3, 7, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (207, 37, 5, 14, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (208, 24, 9, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (209, 21, 2, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (210, 35, 11, 14, 1);

--9C
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (211, 35, 8, 15, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (212, 34, 17, 15, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (213, 24, 9, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (214, 3, 7, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (215, 25, 12, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (216, 27, 3, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (217, 5, 15, 15, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (218, 23, 6, 15, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (219, 8, 4, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (220, 31, 16, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (221, 36, 1, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (222, 11, 2, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (223, 33, 18, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (224, 33, 5, 15, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (225, 33, 11, 15, 1);

--9D
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (226, 37, 5, 16, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (227, 30, 3, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (228, 21, 16, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (229, 23, 6, 16, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (230, 8, 4, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (231, 5, 15, 16, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (232, 37, 18, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (233, 34, 17, 16, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (234, 24, 9, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (235, 35, 8, 16, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (236, 25, 12, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (237, 3, 7, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (238, 35, 1, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (239, 21, 2, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (240, 35, 11, 16, 1);


--Tiết nghỉ
-- khối 6
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (241, 4, 19, 1, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (242, 8, 19, 2, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (243, 23, 19, 3, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (244, 20, 19, 4, 3);

--khối 7
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (245, 26, 19, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (246, 29, 19, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (247, 27, 19, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (248, 31, 19, 8, 2);

-- khối 9
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (249, 36, 19, 13, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (250, 35, 19, 14, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (251, 33, 19, 15, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (252, 37, 19, 16, 1);

-- Tiết chào cờ
-- Khối 6
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (253, null, 13, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (254, null, 13, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (255, null, 13, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (256, null, 13, 4, 1);

--khối 7
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (257, null, 13, 5, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (258, null, 13, 6, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (259, null, 13, 7, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (260, null, 13, 8, 1);

-- khối 9
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (261, null, 13, 13, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (262, null, 13, 14, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (263, null, 13, 15, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (264, null, 13, 16, 1);

-- Khối 8
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (265, null, 13, 9, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (266, null, 13, 10, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (267, null, 13, 11, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (268, null, 13, 12, 1);

-- toán KT
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (269, 11, 20, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (270, 20, 20, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (271, 20, 20, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (272, 20, 20, 4, 2);

insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (273, 26, 20, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (274, 26, 20, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (275, 31, 20, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (276, 31, 20, 8, 2);

insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (277, 10, 20, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (278, 11, 20, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (279, 35, 20, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (280, 36, 20, 12, 2);

insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (281, 36, 20, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (282, 35, 20, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (283, 36, 20, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (284, 35, 20, 16, 2);

-- insert dữ liệu bảng available_teaching_day
INSERT INTO AVAILABLE_TEACHING_DAY (TEACHER_ID, AVAILABLE_DAY) VALUES ('1', '3');
INSERT INTO AVAILABLE_TEACHING_DAY (TEACHER_ID, AVAILABLE_DAY) VALUES ('1', '5');
INSERT INTO AVAILABLE_TEACHING_DAY (TEACHER_ID, AVAILABLE_DAY) VALUES ('7', '2');
INSERT INTO AVAILABLE_TEACHING_DAY (TEACHER_ID, AVAILABLE_DAY) VALUES ('7', '4');
INSERT INTO AVAILABLE_TEACHING_DAY (TEACHER_ID, AVAILABLE_DAY) VALUES ('7', '5');
INSERT INTO AVAILABLE_TEACHING_DAY (TEACHER_ID, AVAILABLE_DAY) VALUES ('22', '6');
INSERT INTO AVAILABLE_TEACHING_DAY (TEACHER_ID, AVAILABLE_DAY) VALUES ('22', '4');
INSERT INTO AVAILABLE_TEACHING_DAY (TEACHER_ID, AVAILABLE_DAY) VALUES ('10', '5');
INSERT INTO AVAILABLE_TEACHING_DAY (TEACHER_ID, AVAILABLE_DAY) VALUES ('10', '7');