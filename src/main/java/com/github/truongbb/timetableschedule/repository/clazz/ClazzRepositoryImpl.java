package com.github.truongbb.timetableschedule.repository.clazz;

import com.github.truongbb.timetableschedule.entity.Clazz;
import com.github.truongbb.timetableschedule.entity.Subject;
import com.github.truongbb.timetableschedule.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClazzRepositoryImpl extends BaseRepository implements ClazzRepository {

    @Override
    public List<Clazz> getAll() {
        return getSession().createQuery("from Clazz", Clazz.class).list();
    }

}
