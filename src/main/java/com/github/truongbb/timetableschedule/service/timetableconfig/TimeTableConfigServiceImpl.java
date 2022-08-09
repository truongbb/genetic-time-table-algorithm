package com.github.truongbb.timetableschedule.service.timetableconfig;

import com.github.truongbb.timetableschedule.entity.Lesson;
import com.github.truongbb.timetableschedule.repository.timetableconfig.LessonRepository;
import com.github.truongbb.timetableschedule.vm.LessonVm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableConfigServiceImpl implements TimeTableConfigService {

    LessonRepository lessonRepository;


    @Override
    public List<LessonVm> getAll() {
        List<Lesson> configLessons = lessonRepository.getAll();
        if (CollectionUtils.isEmpty(configLessons)) {
            return null;
        }
        return configLessons.stream().map(l -> LessonVm.builder()
                .teacherId(l.getTeacher().getId())
                .clazzId(l.getClazz().getId())
                .subjectId(l.getSubject().getId())
                .lessonQuantity(l.getLessonQuantity())
                .build()
        ).collect(Collectors.toList());
    }
}
