package com.github.truongbb.timetableschedule.service.timetableconfig;

import com.github.truongbb.timetableschedule.vm.LessonVm;

import java.util.List;

public interface TimeTableConfigService {

    List<LessonVm> getAll();

}
