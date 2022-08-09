package com.github.truongbb.timetableschedule.service.clazz;

import com.github.truongbb.timetableschedule.entity.Clazz;
import com.github.truongbb.timetableschedule.repository.clazz.ClazzRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClazzServiceImpl implements ClazzService {

    ClazzRepository clazzRepository;

    @Override
    public List<Clazz> getAll() {
        return clazzRepository.getAll();
    }
}
