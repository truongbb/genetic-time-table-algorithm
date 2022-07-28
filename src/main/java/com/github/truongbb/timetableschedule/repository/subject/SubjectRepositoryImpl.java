package com.github.truongbb.timetableschedule.repository.subject;

import com.github.truongbb.timetableschedule.entity.Subject;
import com.github.truongbb.timetableschedule.repository.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class SubjectRepositoryImpl extends BaseRepository implements SubjectRepository {

    @Override
    public List<Subject> getAll() {
        return getSession().createQuery("from Subject", Subject.class).list();
    }

    @Override
    public Subject getStaticSubject(String subjectName) {
        if (StringUtils.isEmpty(subjectName)) {
            return null;
        }
        return getSession()
                .createQuery("select s from Subject s where s.name = :p_name", Subject.class)
                .setParameter("p_name", subjectName)
                .getSingleResult();
    }

}
