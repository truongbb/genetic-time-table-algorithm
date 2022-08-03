package com.github.truongbb.timetableschedule.controller;

import com.github.truongbb.timetableschedule.dto.LessonKey;
import com.github.truongbb.timetableschedule.entity.Lesson;
import com.github.truongbb.timetableschedule.schedule.TimeTableScheduler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableController {

    TimeTableScheduler timeTableScheduler;

    @GetMapping
    public String getTimeTable(ModelMap modelMap) {
        Map<LessonKey, List<Lesson>> result = timeTableScheduler.getTimeTables().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        modelMap.addAttribute("timeTables", result);
        modelMap.addAttribute("allClasses", timeTableScheduler.getClazzes());
        return "index";
    }

}
