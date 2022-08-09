package com.github.truongbb.timetableschedule.vm;

import com.github.truongbb.timetableschedule.entity.Clazz;
import com.github.truongbb.timetableschedule.entity.Subject;
import com.github.truongbb.timetableschedule.entity.Teacher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeTableVm {

    List<Teacher> teachers;
    List<Subject> subjects;
    List<Clazz> clazzes;
    List<LessonVm> configTimeTable;

}
