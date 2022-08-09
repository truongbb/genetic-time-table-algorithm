package com.github.truongbb.timetableschedule.service.teacher;

import com.github.truongbb.timetableschedule.entity.Teacher;
import com.github.truongbb.timetableschedule.repository.teacher.TeacherRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherServiceImpl implements TeacherService {

    TeacherRepository teacherRepository;

    @Override
    public List<Teacher> getAll() {
        return teacherRepository.getAll();
    }

}
