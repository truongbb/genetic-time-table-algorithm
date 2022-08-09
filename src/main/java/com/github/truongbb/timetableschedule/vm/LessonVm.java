package com.github.truongbb.timetableschedule.vm;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonVm {

    Integer teacherId;

    Integer subjectId;

    Integer clazzId;

    Integer lessonQuantity;

}
