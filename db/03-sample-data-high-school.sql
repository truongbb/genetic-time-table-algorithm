
--thêm dữ liệu bảng labs
insert into labs(id, name) VALUES (1,'Phòng tin 1');
insert into labs(id, name) VALUES (2,'Phòng tin 2');
insert into labs(id, name) VALUES (3,'Phòng tin 3');

-- dữ liệu bảng lớp học
insert into clazz(id, name, grade) VALUES (1,'10A1', '10');
insert into clazz(id, name, grade) VALUES (2,'10A2', '10');
insert into clazz(id, name, grade) VALUES (3,'10A3', '10');
insert into clazz(id, name, grade) VALUES (4,'10A4', '10');
insert into clazz(id, name, grade) VALUES (5,'10A5', '10');
insert into clazz(id, name, grade) VALUES (6,'10A6', '10');
insert into clazz(id, name, grade) VALUES (7,'10A7', '10');
insert into clazz(id, name, grade) VALUES (8,'10A8', '10');
insert into clazz(id, name, grade) VALUES (9,'10A9', '10');
insert into clazz(id, name, grade) VALUES (10,'10B', '10');


insert into clazz(id, name, grade) VALUES (11,'11A1', '11');
insert into clazz(id, name, grade) VALUES (12,'11A2', '11');
insert into clazz(id, name, grade) VALUES (13,'11A3', '11');
insert into clazz(id, name, grade) VALUES (14,'11A4', '11');
insert into clazz(id, name, grade) VALUES (15,'11A5', '11');
insert into clazz(id, name, grade) VALUES (16,'11A6', '11');
insert into clazz(id, name, grade) VALUES (17,'11A7', '11');
insert into clazz(id, name, grade) VALUES (18,'11A8', '11');

insert into clazz(id, name, grade) VALUES (19,'12A1', '12');
insert into clazz(id, name, grade) VALUES (20,'12A2', '12');
insert into clazz(id, name, grade) VALUES (21,'12A3', '12');
insert into clazz(id, name, grade) VALUES (22,'12A4', '12');
insert into clazz(id, name, grade) VALUES (23,'12A5', '12');
insert into clazz(id, name, grade) VALUES (24,'12A6', '12');
insert into clazz(id, name, grade) VALUES (25,'12A7', '12');
insert into clazz(id, name, grade) VALUES (26,'12A8', '12');

-- dữ liệu bảng giáo viên
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (1,'Trùng',1,null,1,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (2,'Ng.Vân',0,null,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (3,'D.Giang',0,null,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (4,'Trường',0,null,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (5,'Đg.Hằng',0,null,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (6,'Mai',0,null,12,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (7,'Thành',0,null,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (8,'L.Hương',0,null,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (9,'H.Giang',0,null,8,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (10,'Lý',0,null,7,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (11,'Hoài',0,1,24,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (12,'Duy',0,0,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (20,'Đức',0,1,2,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (21,'Dũng',0,null,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (22,'Quyên',0,null,6,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (23,'Nguyên',0,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (24,'Q.Cường',0,null,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (25,'Ng.Hằng',0,null,5,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (26,'Trường',0,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (27,'Thu',0,1,20,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (28,'Liêm',0,1,3,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (29,'Đông',0,1,10,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (30,'Hiên',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (31,'Thuyên',0,null,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (32,'Son',0,null,4,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (33,'Tuấn',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (34,'Sơn',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (35,'B.Thảo',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (36,'Diện',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (37,'Oanh',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (38,'Quỳnh',0,null ,23,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (39,'Huyền',0,null ,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (40,'Gàng',1,1,17,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (41,'V.Linh',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (42,'N.Giang',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (43,'Đh.Hằng',1,1,9,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (44,'Bích',0,null ,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (45,'Tâm',0,null ,14,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (46,'Thao',1,1,11,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (47,'Mạnh',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (48,'V.Hạnh',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (49,'Hiền',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (50,'Trang',0,null ,18,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (51,'Huệ',1,1,22,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (52,'Dung',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (53,'Hường',1,1,13,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (54,'H.Ngân',0,null ,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (55,'H.Hương',1,1,15,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (56,'Duyên',1,1,16,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (57,'V.Cường',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (58,'Ngoan',0,null ,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (59,'P.Vân',1,1,19,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (60,'Ngoan',0,null ,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (61,'Lan',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (62,'Toàn',0,null ,21,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (63,'Đ.Thảo',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (64,'Tá',0,null ,26,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (65,'Ninh',1,1,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (66,'Sinh',0,null ,null,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (67,'Thảo(A)',1,1,25,0);
insert into teachers(id, name,has_children,skip_days,head_clazz_id,has_farfrom_home) VALUES (68,'L.Hương',0,null ,null,0);


-- dữ liệu bảng subjects
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (1,'Toán',null,0,1,0,'Toán', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (2,'Tin',1,2,0,1,'Tin', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (3,'Tiếng Anh',null,0,0,0,'Anh', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (4,'Sinh học',null,0,1,1,'Sinh', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (5,'Văn',null,2,1,0,'Văn', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (6,'Sử',null,0,0,1,'Sử', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (7,'Địa',null,0,1,1,'Địa', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (8,'Công nghệ',null,0,0,1,'Công nghệ', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (9,'Thể dục',null,0,1,1,'Thể dục', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (10,'KHTN',null,0,0,1,'KHTN', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (11,'Sinh hoạt lớp',null,0,0,0,'SH', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (12,'Hóa học',null,0,1,1,'Hóa', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (13,'Chào cờ',null,0,0,0,'Chào cờ', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (14,'Nhạc',null,0,0,0,'Nhạc', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (15,'Mỹ Thuật',null,0,0,0,'Mỹ Thuật', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (16,'Vật Lý',null,0,0,1,'Vật Lý', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (17,'Công dân',null,0,0,0,'Công dân', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (18,'VănKT',null,2,1,0,'VănKT', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (19,'Nghỉ',null,0,0,0,'Nghỉ', 0);
insert into subjects(id, name,lab_id,block_number,avoid_last_lesson,require_spacing,group_name,priority_number) VALUES (20,'ToánKT',null,0,1,0,'ToánKT', 0);
-- dữ liệu thời khóa biểu của các lớp

--10A1
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (1, 2, 1, 1, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (2, 3, 16, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (3, 4, 17, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (4, 1, 9, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (5, 5, 7, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (6, 6, 3, 1, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (7, 7, 6, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (8, 8, 5, 1, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (9, 9, 8, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (10, 10, 12, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (11, 11, 2, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (12, 12, 4, 1, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (13, 1, 11, 1, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (14, 1, 19, 1, 2);

--10A2
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (15, 8, 5, 2, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (16, 21, 9, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (17, 22, 3, 2, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (18, 3, 16, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (19, 12, 4, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (20, 11, 2, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (21, 23, 7, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (22, 24, 1, 2, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (23, 25, 12, 2, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (24, 4, 17, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (25, 27, 8, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (26, 28, 6, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (27, 20, 11, 2, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (28, 20, 19, 2, 2);

--10A3
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (29, 29, 3, 3, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (30, 12, 4, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (31, 24, 1, 3, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (32, 5, 7, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (33, 3, 16, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (34, 28, 6, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (35, 30, 5, 3, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (36, 31, 12, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (37, 11, 2, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (38, 21, 9, 3, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (39, 27, 8, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (40, 4, 17, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (41, 28, 11, 3, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (42, 28, 19 , 3, 2);

--10A4
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (43, 11, 2, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (44, 27, 8, 4, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (45, 31, 12, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (46, 28, 6, 4, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (47, 32, 3, 4, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (48, 33, 1, 4, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (49, 12, 4, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (50, 21, 9, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (51, 5, 7, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (52, 30, 5, 4, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (53, 3, 16, 4, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (54, 4, 17, 4, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (55, 32, 11, 4, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (56, 32, 19, 4, 2);

--10A5
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (57, 28, 6, 5, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (58, 38, 3, 5, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (59, 8, 5, 5, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (60, 35, 16, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (61, 36, 9, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (62, 23, 7, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (63, 37, 2, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (64, 25, 12, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (65, 27, 8, 5, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (66, 39, 1, 5, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (67, 40, 4, 5, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (68, 4, 17, 5, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (69, 25, 11, 5, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (70, 25, 19, 5, 2);


--10A6
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (71, 31, 12, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (72, 22, 3, 6, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (73, 37, 2, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (74, 35, 16, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (75, 40, 4, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (76, 36, 9, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (77, 23, 7, 6, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (78, 44, 5, 6, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (79, 27, 8, 6, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (80, 28, 6, 6, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (81, 4, 17, 6, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (82, 39, 1, 6, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (83, 22, 11, 6, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (84, 22, 19, 6, 2);

--10A7
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (85, 38, 3, 7, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (86, 4, 17, 7, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (87, 5, 7, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (88, 3, 16, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (89, 41, 4, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (90, 30, 5, 7, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (91, 28, 6, 7, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (92, 9, 8, 7, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (93, 42, 1, 7, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (94, 10, 12, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (95, 36, 9, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (96, 37, 2, 7, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (97, 10, 11, 7, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (98, 10, 19, 7, 2);


--10A8
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (99, 32, 3, 8, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (100, 37, 2, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (101, 30, 5, 8, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (102, 23, 7, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (103, 35, 16, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (104, 41, 4, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (105, 9, 8, 8, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (106, 10, 12, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (107, 42, 1, 8, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (108, 36, 9, 8, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (109, 4, 17, 8, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (110, 28, 6, 8, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (111, 9, 11, 8, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (112, 9, 19, 8, 2);


--10A9
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (113, 22, 3, 9, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (114, 35, 16, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (115, 36, 9, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (116, 9, 8, 9, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (117, 44, 5, 9, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (118, 41, 4, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (119, 43, 12, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (120, 5, 7, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (121, 42, 1, 9, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (122, 50, 6, 9, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (123, 37, 2, 9, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (124, 4, 17, 9, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (125, 43, 11, 9, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (126, 43, 19, 9, 2);


--10B
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (127, 36, 9, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (128, 5, 7, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (129, 29, 3, 10, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (130, 37, 2, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (131, 43, 12, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (132, 50, 6, 10, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (133, 41, 4, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (134, 42, 1, 10, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (135, 9, 8, 10, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (136, 30, 5, 10, 3);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (137, 35, 16, 10, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (138, 4, 17, 10, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (139, 29, 11, 10, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (140, 29, 19, 10, 2);


-- Lớp 11
--11A1
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (141, 45, 3, 11, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (142, 47, 1, 11, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (143, 48, 5, 11, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (144, 40, 4, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (145, 49, 12, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (146, 46, 2, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (147, 50, 6, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (148, 51, 17, 11, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (149, 52, 8, 11, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (150, 21, 9, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (151, 53, 16, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (152, 54, 7, 11, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (153, 46, 11, 11, 1);

--11A2
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (155, 35, 16, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (156, 6, 3, 12, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (157, 57, 1, 12, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (158, 40, 4, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (159, 54, 7, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (160, 49, 12, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (161, 50, 6, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (162, 8, 5, 12, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (163, 46, 2, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (164, 21, 9, 12, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (165, 52, 8, 12, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (166, 51, 17, 12, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (167, 6, 11, 12, 1);



--11A3
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (181, 21, 9, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (182, 55, 4, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (183, 57, 1, 13, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (184, 6, 3, 13, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (185, 8, 5, 13, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (186, 49, 12, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (187, 52, 8, 13, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (188, 51, 17, 13, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (189, 53, 16, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (190, 50, 6, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (191, 5, 7, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (192, 46, 2, 13, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (193, 53, 11, 13, 1);


--11A4
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (196, 58, 5, 14, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (197, 21, 9, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (198, 35, 16, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (199, 45, 3, 14, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (200, 57, 1, 14, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (201, 52, 8, 14, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (202, 46, 2, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (203, 50, 6, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (204, 10, 12, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (205, 51, 17, 14, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (206, 55, 4, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (207, 5, 7, 14, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (208, 45, 11, 14, 1);

--11A5
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (211, 51, 17, 15, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (212, 31, 12, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (213, 58, 5, 15, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (214, 59, 3, 15, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (215, 50, 6, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (216, 2, 1, 15, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (217, 35, 16, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (218, 21, 9, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (219, 61, 8, 15, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (220, 55, 4, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (221, 46, 2, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (222, 23, 7, 15, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (223, 55, 11, 15, 1);

--11A6
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (226, 56, 6, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (227, 45, 3, 16, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (228, 2, 1, 16, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (229, 37, 2, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (230, 23, 7, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (231, 51, 17, 16, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (232, 40, 4, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (233, 48, 5, 16, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (234, 53, 16, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (235, 61, 8, 16, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (236, 10, 12, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (237, 62, 9, 16, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (238, 56, 11, 16, 1);

--11A7
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (241, 48, 5, 17, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (242, 34, 1, 17, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (243, 43, 3, 17, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (244, 31, 12, 17, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (245, 61, 8, 17, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (246, 50, 6, 17, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (247, 40, 4, 17, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (248, 53, 16, 17, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (249, 62, 9, 17, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (250, 51, 17, 17, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (251, 23, 7, 17, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (252, 37, 2, 17, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (253, 40, 11, 17, 1);

--11A8
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (255, 43, 3, 18, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (256, 63, 1, 18, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (257, 51, 17, 18, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (258, 40, 4, 18, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (259, 61, 8, 18, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (260, 58, 5, 18, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (261, 10, 12, 18, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (262, 37, 2, 18, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (263, 50, 6, 18, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (264, 62, 9, 18, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (265, 23, 7, 18, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (266, 35, 16, 18, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (267, 50, 11, 18, 1);

-- 12
-- 12A1
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (269, 4, 17, 19, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (270, 53, 16, 19, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (271, 34, 1, 19, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (272, 46, 2, 19, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (273, 54, 7, 19, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (274, 64, 9, 19, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (275, 59, 3, 19, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (276, 52, 8, 19, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (277, 65, 5, 19, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (278, 56, 6, 19, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (279, 55, 4, 19, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (280, 25, 12, 19, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (281, 59, 11, 19, 1);

-- 12A2
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (282, 51, 17, 20, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (283, 53, 16, 20, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (284, 24, 1, 20, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (285, 46, 2, 20, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (286, 5, 7, 20, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (287, 64, 9, 20, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (288, 59, 3, 20, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (289, 52, 8, 20, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (290, 44, 5, 20, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (291, 56, 6, 20, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (292, 55, 4, 20, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (293, 49, 12, 20, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (294, 27, 11, 20, 1);

-- 12A3
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (295, 4, 17, 21, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (296, 66, 16, 21, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (297, 24, 1, 21, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (298, 46, 2, 21, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (299, 23, 7, 21, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (300, 62, 9, 21, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (301, 29, 3, 21, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (302, 52, 8, 21, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (303, 44, 5, 21, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (304, 56, 6, 21, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (305, 55, 4, 21, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (306, 49, 12, 21, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (307, 62, 11, 21, 1);

-- 12A4
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (308, 51, 17, 22, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (309, 53, 16, 22, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (310, 34, 1, 22, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (311, 46, 2, 22, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (312, 5, 7, 22, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (313, 62, 9, 22, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (314, 22, 3, 22, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (315, 52, 8, 22, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (316, 8, 5, 22, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (317, 67, 6, 22, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (318, 55, 4, 22, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (319, 49, 12, 22, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (320, 51, 11, 22, 1);

-- 12A5
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (321, 4, 17, 23, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (322, 66, 16, 23, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (323, 63, 1, 23, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (324, 46, 2, 23, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (325, 54, 7, 23, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (326, 64, 9, 23, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (327, 38, 3, 23, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (328, 20, 8, 23, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (329, 65, 5, 23, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (330, 67, 6, 23, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (331, 12, 4, 23, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (332, 49, 12, 23, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (333, 38, 11, 23, 1);

-- 12A6
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (334, 4, 17, 24, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (335, 3, 16, 24, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (336, 63, 1, 24, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (337, 11, 2, 24, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (338, 54, 7, 24, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (339, 64, 9, 24, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (340, 38, 3, 24, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (341, 20, 8, 24, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (342, 8, 5, 24, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (343, 67, 6, 24, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (344, 12, 4, 24, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (345, 25, 12, 24, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (346, 11, 11, 24, 1);

-- 12A7
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (347, 4, 17, 25, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (348, 3, 16, 25, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (349, 47, 1, 25, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (350, 11, 2, 25, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (351, 54, 7, 25, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (352, 64, 9, 25, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (353, 43, 3, 25, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (354, 20, 8, 25, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (355, 65, 5, 25, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (356, 67, 6, 25, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (357, 12, 4, 25, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (358, 25, 12, 25, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (359, 67, 11, 25, 1);

-- 12A8
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (360, 51, 17, 26, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (361, 3, 16, 26, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (362, 47, 1, 26, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (363, 11, 2, 26, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (364, 5, 7, 26, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (365, 64, 9, 26, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (366, 32, 3, 26, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (367, 20, 8, 26, 1);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (368, 44, 5, 26, 4);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (369, 28, 6, 26, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (370, 12, 4, 26, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (371, 25, 12, 26, 2);
insert into time_table_config (id, teacher_id, subject_id, clazz_id, lesson_quantity) values (372, 64, 11, 26, 1);



-- insert dữ liệu bảng available_teaching_day
INSERT INTO AVAILABLE_TEACHING_DAY (ID, TEACHER_ID, AVAILABLE_DAY) VALUES (1, '1', '3');
INSERT INTO AVAILABLE_TEACHING_DAY (ID, TEACHER_ID, AVAILABLE_DAY) VALUES (2, '1', '5');
INSERT INTO AVAILABLE_TEACHING_DAY (ID, TEACHER_ID, AVAILABLE_DAY) VALUES (3, '7', '2');
INSERT INTO AVAILABLE_TEACHING_DAY (ID, TEACHER_ID, AVAILABLE_DAY) VALUES (4, '7', '4');
INSERT INTO AVAILABLE_TEACHING_DAY (ID, TEACHER_ID, AVAILABLE_DAY) VALUES (5, '7', '5');
INSERT INTO AVAILABLE_TEACHING_DAY (ID, TEACHER_ID, AVAILABLE_DAY) VALUES (8, '10', '5');
INSERT INTO AVAILABLE_TEACHING_DAY (ID, TEACHER_ID, AVAILABLE_DAY) VALUES (9, '10', '3');
INSERT INTO AVAILABLE_TEACHING_DAY (ID, TEACHER_ID, AVAILABLE_DAY) VALUES (10, '10', '4');
INSERT INTO AVAILABLE_TEACHING_DAY (ID, TEACHER_ID, AVAILABLE_DAY) VALUES (11, '10', '2');
