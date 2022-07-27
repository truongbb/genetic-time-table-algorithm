package com.github.truongbb.timetableschedule.repository.subject;

import com.github.truongbb.timetableschedule.entity.Subject;

import java.util.List;

public interface SubjectRepository {

    List<Subject> getAll();

    Subject getStaticSubject(String subjectName);

}
