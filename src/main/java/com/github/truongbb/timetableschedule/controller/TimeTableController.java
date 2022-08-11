package com.github.truongbb.timetableschedule.controller;

import com.github.truongbb.timetableschedule.constant.TimeTableConstants;
import com.github.truongbb.timetableschedule.dto.LessonKey;
import com.github.truongbb.timetableschedule.entity.Lesson;
import com.github.truongbb.timetableschedule.schedule.TimeTableScheduler;
import com.github.truongbb.timetableschedule.service.timetable.TimeTableService;
import com.github.truongbb.timetableschedule.vm.TimeTableVm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/time-table")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableController {

    TimeTableScheduler timeTableScheduler;

    TimeTableService timeTableService;

    @GetMapping
    public String getTimeTable(ModelMap modelMap) {
        timeTableScheduler.getBestResultsTimeTable().entrySet().forEach(lessonKeyListEntry -> {
            List<Lesson> lessons = lessonKeyListEntry.getValue();
            for (int i = 0; i < lessons.size(); i++) {
                Lesson l1 = lessons.get(i);
                for (int j = i + 1; j < lessons.size(); j++) {
                    Lesson l2 = lessons.get(j);
                    if (!ObjectUtils.isEmpty(l1.getTeacher()) && !ObjectUtils.isEmpty(l2.getTeacher()) && l1.getTeacher().getId().equals(l2.getTeacher().getId())
                            && !l1.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && !l2.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)) {
                        l1.setDuplicated(true);
                        l2.setDuplicated(true);
                    }
                }
            }
        });
        Map<LessonKey, List<Lesson>> result = timeTableScheduler.getTimeTables().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        modelMap.addAttribute("timeTables", result);
        modelMap.addAttribute("allClasses", timeTableScheduler.getClazzes());
        return "index";
    }

    @PostMapping
    public ResponseEntity<Map<LessonKey, List<Lesson>>> generateTimeTable(@RequestBody TimeTableVm timeTableVm) {
        Map<LessonKey, List<Lesson>> result = timeTableService.generateTable(timeTableVm).entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
