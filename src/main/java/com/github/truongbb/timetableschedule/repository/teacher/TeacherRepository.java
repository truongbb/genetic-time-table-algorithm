package com.github.truongbb.timetableschedule.repository.teacher;

import com.github.truongbb.timetableschedule.entity.Teacher;

import java.util.List;

public interface TeacherRepository {

    List<Teacher> getAll();

    Teacher findHeadTeacher(String className);

}
