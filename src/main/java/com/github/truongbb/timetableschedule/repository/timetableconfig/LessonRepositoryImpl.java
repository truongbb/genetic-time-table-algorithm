package com.github.truongbb.timetableschedule.repository.timetableconfig;

import com.github.truongbb.timetableschedule.entity.Lesson;
import com.github.truongbb.timetableschedule.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LessonRepositoryImpl extends BaseRepository implements LessonRepository {
    @Override
    public List<Lesson> getAll() {
        return getSession()
                .createQuery("select l from Lesson l join Subject s on s.id = l.subject.id where s.name <> 'Chào cờ' and s.name <> 'Sinh hoạt lớp'", Lesson.class)
                .list();
    }
}
