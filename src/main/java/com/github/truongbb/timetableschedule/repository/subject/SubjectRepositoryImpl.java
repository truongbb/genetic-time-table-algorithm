package com.github.truongbb.timetableschedule.repository.subject;

import com.github.truongbb.timetableschedule.entity.Subject;
import com.github.truongbb.timetableschedule.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubjectRepositoryImpl extends BaseRepository implements SubjectRepository {

    @Override
    public List<Subject> getAll() {
        return getSession().createQuery("from Subject", Subject.class).list();
    }

    @Override
    public Subject getStaticSubject(String subjectName) {
        return getSession().createQuery("from Subject s where s.name = " + subjectName, Subject.class).getSingleResult();
    }

}
