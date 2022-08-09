package com.github.truongbb.timetableschedule.service.timetable;

import com.github.truongbb.timetableschedule.dto.LessonKey;
import com.github.truongbb.timetableschedule.entity.Lesson;
import com.github.truongbb.timetableschedule.vm.TimeTableVm;

import java.util.List;
import java.util.Map;

public interface TimeTableService {

    Map<LessonKey, List<Lesson>> generateTable(TimeTableVm timeTableVm) throws IllegalArgumentException;

}
