package com.github.truongbb.timetableschedule.service.subject;

import com.github.truongbb.timetableschedule.entity.Subject;
import com.github.truongbb.timetableschedule.repository.subject.SubjectRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectServiceImpl implements SubjectService {

    SubjectRepository subjectRepository;

    @Override
    public List<Subject> getAll() {
        return subjectRepository.getAll();
    }

}
