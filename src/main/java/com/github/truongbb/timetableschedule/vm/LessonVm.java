package com.github.truongbb.timetableschedule.vm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonVm {

    Integer teacherId;

    Integer subjectId;

    Integer clazzId;

    Integer lessonQuantity;

}
