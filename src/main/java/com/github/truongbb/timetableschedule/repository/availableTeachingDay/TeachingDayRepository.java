package com.github.truongbb.timetableschedule.repository.availableTeachingDay;

import com.github.truongbb.timetableschedule.entity.AvailableTeachingDay;
import com.github.truongbb.timetableschedule.entity.Teacher;

import java.util.List;

public interface TeachingDayRepository {

    List<AvailableTeachingDay> getAll();

    List<AvailableTeachingDay> findTeachingDayTeacher1(int teacherid);

}
