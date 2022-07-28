-- phòng thực hành, máy tính
create table labs
(
    id   int primary key,
    name nvarchar2(500) not null
);

-- lớp
create table clazz
(
    id   int primary key,
    name nvarchar2(500) not null
);

-- môn học
create table subjects
(
    id                int primary key,
    name              nvarchar2(500) not null,
    lab_id            number,
    block_number      int,
    avoid_last_lesson number(1,0) default 0,
    require_spacing   number(1,0) default 0,
    group_name        nvarchar2(500),
    priority_number   number default 0 not null,
    CONSTRAINT fk_subject_lab FOREIGN KEY (lab_id) REFERENCES labs (id)
);

-- giáo viên
create table teachers
(
    id            int primary key,
    name          nvarchar2(500) not null,
    has_children  number(1,0) default 0 not null,
    skip_days     nvarchar2(2000),
    head_clazz_id number,
    constraint fk_teacher_clazz foreign key (clazz_id) references clazz (id)
);

-- thời khóa biểu
create table time_table
(
    id              number not null,
    teacher_id      number,
    subject_id      number not null,
    clazz_id        number not null,
    lesson_quantity number not null,
    primary key (id),
    constraint fk_time_table_clazz foreign key (clazz_id) references clazz (id),
    constraint fk_time_table_teacher foreign key (teacher_id) references teachers (id),
    constraint fk_time_table_subject foreign key (subject_id) references subjects (id)
);
