package com.github.truongbb.timetableschedule.repository.teacher;

import com.github.truongbb.timetableschedule.entity.Teacher;
import com.github.truongbb.timetableschedule.repository.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class TeacherRepositoryImpl extends BaseRepository implements TeacherRepository {

    @Override
    public List<Teacher> getAll() {
        return getSession().createQuery("from Teacher", Teacher.class).list();
    }

    @Override
    public Teacher findHeadTeacher(String className) {
        if (StringUtils.isEmpty(className)) {
            return null;
        }
        return getSession()
                .createQuery("select t from Teacher t join Clazz c on c.id = t.headClazz.id where c.name = :p_class_name", Teacher.class)
                .setParameter("p_class_name", className)
                .getSingleResult();
    }

}
