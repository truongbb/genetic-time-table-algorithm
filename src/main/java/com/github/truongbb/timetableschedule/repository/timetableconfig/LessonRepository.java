package com.github.truongbb.timetableschedule.repository.timetableconfig;

import com.github.truongbb.timetableschedule.entity.Lesson;

import java.util.List;

public interface LessonRepository {

    List<Lesson> getAll();

}
