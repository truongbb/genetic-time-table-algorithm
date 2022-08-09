package com.github.truongbb.timetableschedule.repository.availableTeachingDay;

import com.github.truongbb.timetableschedule.entity.AvailableTeachingDay;
import com.github.truongbb.timetableschedule.entity.Teacher;
import com.github.truongbb.timetableschedule.repository.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class TeachingDayRepositoryImpl extends BaseRepository implements TeachingDayRepository {


    @Override
    public List<AvailableTeachingDay> getAll() {
        return getSession().createQuery("from AvailableTeachingDay ", AvailableTeachingDay.class).list();
    }

    @Override
    public List<AvailableTeachingDay> findTeachingDayTeacher1(int teacherid) {
        if (StringUtils.isEmpty(teacherid)) {
            return null;
        }
        return getSession()
                .createQuery("select t from Teacher t join AvailableTeachingDay a on t.id = a.teacher where t.id = :p_id", AvailableTeachingDay.class)
                .setParameter("p_id", teacherid)
                .getResultList();
    }
}
